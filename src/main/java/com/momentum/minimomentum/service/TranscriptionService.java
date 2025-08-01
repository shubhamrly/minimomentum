package com.momentum.minimomentum.service;


import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.TranscriptionRepository;
import com.momentum.minimomentum.service.openAi.OpenAiClient;
import com.momentum.minimomentum.utils.PromptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranscriptionService {
    @Autowired
    private OpenAiClient openAiClient;
    @Autowired
    private TranscriptionRepository transcriptRepository;


    public TranscriptResponseDTO generateTranscript(String language) {
        String prompt = PromptUtils.getPrompt(PromptType.GENERATION_PROMPT, language);
        String content = openAiClient.getCompletion(prompt);
        Transcript transcript = createAndSaveTranscripts(content, language);
        return new TranscriptResponseDTO(transcript.getId(), content, language, transcript.getCreateDateTime());
    }

    public Transcript createAndSaveTranscripts(String content, String language) {
        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setTranscriptText(content);
        transcript.setCreateDateTime(LocalDateTime.now());
        return transcriptRepository.save(transcript);
    }

    public Transcript getTranscriptById(Long id) {
        Transcript transcript = transcriptRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transcript not found by id: " + id));
        return new Transcript(transcript.getId(), transcript.getTranscriptText(), transcript.getLanguage(), transcript.getCreateDateTime());
    }

    public List<TranscriptResponseDTO> getAllTranscripts() {
        List<Transcript> transcriptList = transcriptRepository.findAll();

        if (transcriptList.isEmpty()) {
            throw new EntityNotFoundException("No transcripts found.");
        }

        return transcriptList.stream()
                .map(t -> new TranscriptResponseDTO(t.getId(), t.getTranscriptText(), t.getLanguage(), t.getCreateDateTime()))
                .collect(Collectors.toList());
    }


}



