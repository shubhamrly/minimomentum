package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import com.momentum.minimomentum.utils.PromptUtils;
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

    public String getAnswersByTranscriptId(Long transcriptId, String question) {

        String transcriptText = generationService.getTranscriptById(transcriptId).getTranscriptText();

        String prompt = PromptUtils.getPrompt(PromptType.QUESTION_ANSWERING_PROMPT, "english");
        String promptWithHistory = String.format("""
                        %s
                        transcript: %s
                        Question and Answer History sorted by latest first: %s
                        
                        Question: %s
                        """,
                prompt,
                transcriptText,
                getAllQAByTranscriptIdInternal(transcriptId),
                question
        );

        String content = openAiClient.getCompletionOpenAi(promptWithHistory);
        log.info(" QuestionAnswerService || Prompt length : {}  and response length {}", prompt.length(), content.length());
        return createAndSaveQuestionAnswer(transcriptId, question, content).getAnswer();
    }

    public List<TranscriptQAResponseDTO> getAllQAByTranscriptIdInternal(Long transcriptId) {
        List<QuestionAnswer> questionAnswers = questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId);
        return toTranscriptQAResponseDTOList(questionAnswers);
    }

    public List<TranscriptQAResponseDTO> getAllQAByTranscriptId(Long transcriptId) {
        List<TranscriptQAResponseDTO> questionAnswers =  getAllQAByTranscriptIdInternal(transcriptId);
        if (questionAnswers.isEmpty()) {
            log.info("No question answers found for transcriptId: {}", transcriptId);
            throw new EntityNotFoundException("No question answers found for transcriptId: " + transcriptId);
        }
        return questionAnswers;
    }

    public QuestionAnswer createAndSaveQuestionAnswer(Long transcriptId, String question, String answer) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        Transcript transcript = generationService.getTranscriptById(transcriptId);
        questionAnswer.setTranscript(transcript);
        questionAnswer.setQuestion(question);
        questionAnswer.setAnswer(answer);
        questionAnswer.setCreateDateTime(LocalDateTime.now());
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
