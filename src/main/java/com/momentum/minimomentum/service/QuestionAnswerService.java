package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import com.momentum.minimomentum.service.openAi.OpenAiClient;
import com.momentum.minimomentum.utils.PromptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class QuestionAnswerService {
    @Autowired
    TranscriptionService generationService;
    @Autowired
    QuestionAnswersRepository questionAnswersRepository;
    @Autowired
    OpenAiClient openAiClient;

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
                getAllQAByTranscriptId(transcriptId),
                question
        );

        String content = openAiClient.getCompletion(promptWithHistory);
        log.info(" QuestionAnswerService || Prompt length : {}  and response length {}", prompt.length(), content.length());
        return createAndSaveQuestionAnswer(transcriptId, question, content).getAnswer();
    }

    public List<QuestionAnswer> getAllQAByTranscriptId(Long transcriptId) {
        List<QuestionAnswer> questionAnswers = questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(Long.valueOf(transcriptId));
        if (questionAnswers.isEmpty()) {
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
}
