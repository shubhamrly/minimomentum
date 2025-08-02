package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionAnswerService {

    private final TranscriptionService generationService;

    private final QuestionAnswersRepository questionAnswersRepository;

    private final OpenAiClient openAiClient;

    private final String className = getClass().getSimpleName();

    public String getAnswersByTranscriptId(Long transcriptId, String question) {

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
                question
        ).replaceAll("\\s+", " ").trim();
        String content = openAiClient.getCompletionOpenAi(promptWithHistory);

        log.info("[{}] Generated answer for question: {}", className , question);

        return createAndSaveQuestionAnswer(transcriptId, question, content).getAnswer();
    }

    public List<TranscriptQAResponseDTO> getAllQAByTranscriptIdInternal(Long transcriptId) {

        List<QuestionAnswer> questionAnswers = questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId);

        log.debug("[{}] [Internal] Fetched {} question answers for transcriptId: {}", className, questionAnswers.size(), transcriptId);

        return toTranscriptQAResponseDTOList(questionAnswers);
    }

    public List<TranscriptQAResponseDTO> getAllQAByTranscriptId(Long transcriptId) {
        List<TranscriptQAResponseDTO> questionAnswers = getAllQAByTranscriptIdInternal(transcriptId);
        if (questionAnswers.isEmpty()) {
            throw new EntityNotFoundException("No question answers found for transcriptId: " + transcriptId);
        }
        log.info("[{}] Returning {} question answers for transcriptId: {}", className, questionAnswers.size(), transcriptId);
        return questionAnswers;
    }

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

    public TranscriptQAResponseDTO toTranscriptQAResponseDTO(QuestionAnswer questionAnswer) {
        TranscriptQAResponseDTO responseDTO = new TranscriptQAResponseDTO();
        responseDTO.setId(String.valueOf(questionAnswer.getId()));
        responseDTO.setQuestion(questionAnswer.getQuestion());
        responseDTO.setAnswer(questionAnswer.getAnswer());
        responseDTO.setTranscriptId(questionAnswer.getTranscript().getId());
        responseDTO.setCreateDateTime(questionAnswer.getCreateDateTime());
        return responseDTO;
    }

    public List<TranscriptQAResponseDTO> toTranscriptQAResponseDTOList(List<QuestionAnswer> questionAnswerList) {
        return questionAnswerList.stream()
                .map(this::toTranscriptQAResponseDTO)
                .toList();
    }

}
