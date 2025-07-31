package com.momentum.minimomentum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.SummaryDetailsDTO;
import com.momentum.minimomentum.dto.SummaryResponseDTO;
import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.SummaryNotFoundException;
import com.momentum.minimomentum.model.Summary;
import com.momentum.minimomentum.repository.SummaryRepository;
import com.momentum.minimomentum.utils.PromptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryService {
    @Autowired
    GenerationService generationService;
    @Autowired
    private OpenAiClient openAiClient;
    @Autowired
    private SummaryRepository summaryRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public SummaryResponseDTO generateSummary(String transcriptId, String language) {
        TranscriptResponseDTO transcript = generationService.getTranscript(transcriptId);
        String prompt = PromptUtils.getPrompt(PromptType.SUMMARY_PROMPT, language) + " \n\n " + transcript.getTranscriptText();
        String content = openAiClient.getCompletion(prompt);
        Summary summary = saveOrUpdateSummary(content, transcriptId, language);
        return new SummaryResponseDTO(summary.getId(), summary.getSummary(), summary.getTranscriptId(), summary.getLanguage());
    }

    private Summary saveOrUpdateSummary(String content, String transcriptId, String language) {
        SummaryDetailsDTO summaryDetails;
        try {
            summaryDetails = objectMapper.readValue(content, SummaryDetailsDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse summary content to DTO", e);
        }
        Summary summary = summaryRepository.findByTranscriptIdAndLanguage(transcriptId, language)
                .map(existingSummary -> {
                    existingSummary.setSummary(summaryDetails);
                    return existingSummary;
                })
                .orElseGet(() -> {
                    Summary newSummary = new Summary();
                    newSummary.setTranscriptId(transcriptId);
                    newSummary.setLanguage(language);
                    newSummary.setSummary(summaryDetails);
                    return newSummary;
                });

        return summaryRepository.save(summary);
    }


    public SummaryResponseDTO getSummary(String summaryId) {
        Summary summary = summaryRepository.findById(summaryId).orElseThrow(() -> new SummaryNotFoundException("Summary not found by id: " + summaryId));
        return new SummaryResponseDTO(summary.getId(), summary.getSummary(), summary.getTranscriptId(), summary.getLanguage());
    }

    public List<SummaryResponseDTO> getAllSummaries() {
        List<Summary> summaryList = summaryRepository.findAll();
        if (summaryList.isEmpty()) {
            throw new SummaryNotFoundException("No summaries found.");
        }
        return summaryList.stream()
                .map(s -> new SummaryResponseDTO(s.getId(), s.getSummary(), s.getTranscriptId(), s.getLanguage()))
                .toList();
    }
}
