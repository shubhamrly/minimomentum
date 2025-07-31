package com.momentum.minimomentum.service;


import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.TranscriptNotFoundException;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.prompt.impl.PromptFactory;
import com.momentum.minimomentum.repository.TranscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenerationService {
        @Autowired
        private  OpenAiClient openAiClient;
        @Autowired
        private TranscriptionRepository transcriptRepository;
        @Autowired
        private PromptFactory promptFactory;


       public TranscriptResponseDTO generateTranscript(String language) {
        String prompt = promptFactory.getPrompt(PromptType.GENERATION_PROMPT, language);
        String content = openAiClient.getCompletion(prompt);
           Transcript transcript = createAndSaveTranscripts(content, language);
           return new TranscriptResponseDTO(transcript.getId(), content, language, transcript.getCreatedAt());
    }

    public Transcript createAndSaveTranscripts(String content, String language) {
        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setTranscriptText(content);
        transcript.setCreatedAt(LocalDateTime.now());
        return transcriptRepository.save(transcript);
        }

    public TranscriptResponseDTO  getTranscript(String id){
       Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new TranscriptNotFoundException("Transcript not found by id: "+ id));
        return new TranscriptResponseDTO(transcript.getId(),transcript.getTranscriptText(), transcript.getLanguage(),  transcript.getCreatedAt());
    }

    public List<TranscriptResponseDTO> getAllTranscripts() {
        List<Transcript> transcriptList = transcriptRepository.findAll();

        if (transcriptList.isEmpty()) {
            throw new TranscriptNotFoundException("No transcripts found.");
        }

        return transcriptList.stream()
                .map(t -> new TranscriptResponseDTO(t.getId(),t.getLanguage(), t.getTranscriptText(), t.getCreatedAt()))
                .collect(Collectors.toList());
    }


}



