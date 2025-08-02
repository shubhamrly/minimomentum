package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.TranscriptionRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import com.momentum.minimomentum.utils.PromptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranscriptionService {

    private final OpenAiClient openAiClient;

    private final TranscriptionRepository transcriptRepository;

    public TranscriptResponseDTO generateTranscript(String language) {
        String prompt = PromptUtils.getPrompt(PromptType.GENERATION_PROMPT, language);
        String content = openAiClient.getCompletionOpenAi(prompt);
        Transcript transcript = createAndSaveTranscripts(content, language);
        return toTranscriptResponseDTO(transcript);
    }

    public Transcript createAndSaveTranscripts(String content, String language) {
        Transcript transcript = new Transcript();
        transcript.setLanguage(language);
        transcript.setTranscriptText(content);
        transcript.setCreateDateTime(LocalDateTime.now());
        return transcriptRepository.save(transcript);
    }

    public Transcript getTranscriptById(Long id) {
        return transcriptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transcript not found by id: " + id));

        //return new Transcript(transcript.getId(), transcript.getTranscriptText(), transcript.getLanguage(), transcript.getCreateDateTime());
    }

    public TranscriptResponseDTO getTranscriptDtoById(Long id) {
        Transcript transcript = getTranscriptById(id);
        return toTranscriptResponseDTO(transcript);
    }

    public List<TranscriptResponseDTO> getAllTranscripts() {
        List<Transcript> transcriptList = transcriptRepository.findAll();

        if (transcriptList.isEmpty()) {
            throw new EntityNotFoundException("No transcripts found.");
        }

        return transcriptList.stream()
                .map(this::toTranscriptResponseDTO)
                .collect(Collectors.toList());
    }

    public TranscriptResponseDTO toTranscriptResponseDTO(Transcript transcript) {
        TranscriptResponseDTO responseDto = new TranscriptResponseDTO();
        responseDto.setId(transcript.getId());
        responseDto.setTranscriptText(transcript.getTranscriptText());
        responseDto.setLanguage(transcript.getLanguage());
        responseDto.setCreateDateTime(transcript.getCreateDateTime());
        return responseDto;
    }
}
