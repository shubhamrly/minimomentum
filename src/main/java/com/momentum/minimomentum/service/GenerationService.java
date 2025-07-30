package com.momentum.minimomentum.service;



import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.GenerationResponseDTO;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.prompt.impl.PromptFactory;
import com.momentum.minimomentum.repository.TranscriptionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GenerationService {
        @Autowired
        private  OpenAiClient openAiClient;
        @Autowired
        private  TranscriptionsRepo transcriptRepository;
        @Autowired
        private PromptFactory promptFactory;


       public GenerationResponseDTO generateTranscript(String language) {
        String prompt = promptFactory.getPrompt(PromptType.GENERATION_PROMPT, language);
        String content = openAiClient.getCompletion(prompt);
        String transcriptId = createAndSaveTranscripts(content, language);
        return new GenerationResponseDTO(transcriptId,content);
    }

    public String createAndSaveTranscripts(String content, String language) {
        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setContent(content);
        transcript.setCreatedAt(LocalDateTime.now());
        return transcriptRepository.save(transcript).getId();
        };
    }



