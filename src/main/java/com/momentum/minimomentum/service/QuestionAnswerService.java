package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import jakarta.persistence.Converts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/*
 * Service for handling question-answer pairs related to transcripts.
 * It provides methods to generate answers based on transcripts, retrieve all Q&A pairs,
 * and save new question-answer pairs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final TranscriptionService generationService;

    private final QuestionAnswersRepository questionAnswersRepository;

    private final OpenAiClient openAiClient;

    private final String className = getClass().getSimpleName();

    /**
     * Answers a question based on the generated transcript by transcriptId.
     * Generates answers to questions based on the provided transcript ID.
     *
     * @param transcriptId the ID of the transcript to answer questions for
     * @param question the question to be answered
     * @return the answer to the question
     */
    public String getAnswersByTranscriptId(Long transcriptId, String question) {

        String sanitizedQuestion = question.replaceAll("[^a-zA-Z0-9,./?!\\s]", "");

        String transcriptText = generationService.getTranscriptById(transcriptId).getTranscriptText();

        String promptWithHistory = String.format("""
                        %s
                        transcript: %s
                        Question and Answer History sorted by latest first: %s
                        
                        Question: %s
                        """,
                PromptConstants.QUESTION_ANSWER_PROMPT_CONSTANT,
                transcriptText,
                getAllQAByTranscriptIdInternal(transcriptId),
                sanitizedQuestion
        ).replaceAll("\\s+", " ").trim();
        String content = openAiClient.getCompletionOpenAi(promptWithHistory);

        log.info("[{}] Generated answer for sanitized question: {}", className, sanitizedQuestion);

        return createAndSaveQuestionAnswer(transcriptId, sanitizedQuestion, content).getAnswer();
    }

    /**
     * Retrieves all previously asked questions and their answers by
     * transcriptId. Fetches all previously asked questions and their answers
     * ordered by latest dateTime based on the provided transcript ID.
     *
     * @param transcriptId the ID of the transcript to retrieve answers for
     * @return a list of TranscriptQAResponseDTO with all questions and answers
     */
    public List<TranscriptQAResponseDTO> getAllQAByTranscriptIdInternal(Long transcriptId) {

        List<QuestionAnswer> questionAnswers = questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId);

        log.debug("[{}] [Internal] Fetched {} question answers for transcriptId: {}", className, questionAnswers.size(), transcriptId);

        return toTranscriptQAResponseDTOList(questionAnswers);
    }

    /**
     * Retrieves all previously asked questions and their answers by
     * transcriptId. If no question answers are found, it throws an
     * EntityNotFoundException.
     *
     * @param transcriptId the ID of the transcript to retrieve answers for
     * @return a list of TranscriptQAResponseDTO with all questions and answers
     */
    public List<TranscriptQAResponseDTO> getAllQAByTranscriptId(Long transcriptId) {
        List<TranscriptQAResponseDTO> questionAnswers = getAllQAByTranscriptIdInternal(transcriptId);
        if (questionAnswers.isEmpty()) {
            throw new EntityNotFoundException("No question answers found for transcriptId: " + transcriptId);
        }
        log.info("[{}] Returning {} question answers for transcriptId: {}", className, questionAnswers.size(), transcriptId);
        return questionAnswers;
    }

    /**
     * Creates and saves a new question-answer pair associated with a
     * transcript.
     *
     * @param transcriptId the ID of the transcript to associate with the
     * question-answer pair
     * @param question the question to be saved
     * @param answer the answer to be saved
     * @return the saved QuestionAnswer entity
     */

    public QuestionAnswer createAndSaveQuestionAnswer(Long transcriptId, String question, String answer) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        Transcript transcript = generationService.getTranscriptById(transcriptId);
        questionAnswer.setTranscript(transcript);
        questionAnswer.setQuestion(question);
        questionAnswer.setAnswer(answer);
        questionAnswer.setCreateDateTime(LocalDateTime.now());
        log.info("[{}] Saving question answer for transcriptId: {}, question: {}", className, transcriptId, question);
        return questionAnswersRepository.save(questionAnswer);
    }

    /** AI Generated-IDE Suggestion
     * Converts a QuestionAnswer entity to a TranscriptQAResponseDTO.
     *
     * @param questionAnswer the QuestionAnswer entity to convert
     * @return the converted TranscriptQAResponseDTO
     */
    public TranscriptQAResponseDTO toTranscriptQAResponseDTO(QuestionAnswer questionAnswer) {
        TranscriptQAResponseDTO responseDTO = new TranscriptQAResponseDTO();
        responseDTO.setId(String.valueOf(questionAnswer.getId()));
        responseDTO.setQuestion(questionAnswer.getQuestion());
        responseDTO.setAnswer(questionAnswer.getAnswer());
        responseDTO.setTranscriptId(questionAnswer.getTranscript().getId());
        responseDTO.setCreateDateTime(questionAnswer.getCreateDateTime());
        return responseDTO;
    }

    /**
     * Converts a list of QuestionAnswer entities to a list of
     * TranscriptQAResponseDTO.
     *
     * @param questionAnswerList the list of QuestionAnswer entities to convert
     * @return a list of converted TranscriptQAResponseDTO
     */
    public List<TranscriptQAResponseDTO> toTranscriptQAResponseDTOList(List<QuestionAnswer> questionAnswerList) {
        return questionAnswerList.stream()
                .map(this::toTranscriptQAResponseDTO)
                .toList();
    }

}
