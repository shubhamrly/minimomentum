package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.prompt.impl.PromptFactory;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class QuestionAnswerService {
    @Autowired
    GenerationService generationService;
    @Autowired
    QuestionAnswersRepository questionAnswersRepository;
    @Autowired
    PromptFactory promptFactory;
    @Autowired
    OpenAiClient openAiClient;

    public String getAnswersByTranscriptId(String transcriptId,String question) {

        String transcriptText = generationService.getTranscript(transcriptId).getTranscriptText();
        log.info("Transcript text for ID {}: {}", transcriptId, transcriptText);
        String prompt = promptFactory.getPrompt(PromptType.QUESTION_ANSWERING_PROMPT, "english")+" \n\n " + "transcript :" + transcriptText + "    Question and Answer History sorted by latest first: " + getAllQAByTranscriptId(transcriptId)+ "\n\n" + " Question: " + question;
        log.info("Generated prompt for question answering: {}", prompt);
        String content = openAiClient.getCompletion(prompt);

        return createAndSaveQuestionAnswer(transcriptId, question, content).getAnswer();
    }

    public List<QuestionAnswer> getAllQAByTranscriptId(String transcriptId) {
        return Optional.ofNullable(questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId))
                .orElse(Collections.emptyList());
    }

    public QuestionAnswer createAndSaveQuestionAnswer(String transcriptId, String question, String answer) {
        QuestionAnswer questionAnswer = new QuestionAnswer();
        questionAnswer.setTranscriptId(transcriptId);
        questionAnswer.setQuestion(question);
        questionAnswer.setAnswer(answer);
        questionAnswer.setCreateDateTime(LocalDateTime.now());
        return questionAnswersRepository.save(questionAnswer);
    }
}
