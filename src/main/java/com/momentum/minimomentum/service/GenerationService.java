package com.momentum.minimomentum.service;


import com.momentum.minimomentum.dto.GenerationResponseDTO;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.TranscriptionsRepo;
import com.momentum.minimomentum.utils.TranscriptGenerationPromptBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GenerationService {
        @Autowired
        private  OpenAiClient openAiClient;
        @Autowired
        private  TranscriptionsRepo transcriptRepository;

    public GenerationResponseDTO generateTranscript(String language) {
        String prompt = TranscriptGenerationPromptBuilder.build(language);
        String content = openAiClient.getCompletion(prompt);

        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setContent(content);
        transcript.setCreatedAt(LocalDateTime.now());
        Transcript savedTranscript= transcriptRepository.save(transcript);

        return new GenerationResponseDTO(savedTranscript.getId(),content);
    }
}



