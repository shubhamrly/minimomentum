package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.SummaryResponseDTO;
import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.SummaryNotFoundException;
import com.momentum.minimomentum.model.Summary;
import com.momentum.minimomentum.prompt.impl.PromptFactory;
import com.momentum.minimomentum.repository.SummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryService {
    @Autowired
    GenerationService generationService;
    @Autowired
    private PromptFactory promptFactory;
    @Autowired
    private  OpenAiClient openAiClient;
    @Autowired
    private SummaryRepository summaryRepository;

    public SummaryResponseDTO generateSummary(String transcriptId,String language) {
        TranscriptResponseDTO transcript = generationService.getTranscript(transcriptId);
        String prompt = promptFactory.getPrompt(PromptType.SUMMARY_PROMPT, language) +" \n\n " + transcript.getContent();
        String content = openAiClient.getCompletion(prompt);
        Summary summary = saveOrUpdateSummary(content,transcriptId,language);
        return new SummaryResponseDTO(summary.getId(), summary.getSummary(), summary.getTranscriptId(), summary.getLanguage());
        }

    private Summary saveOrUpdateSummary(String content, String transcriptId, String language) {
        Summary summary = summaryRepository.findByTranscriptIdAndLanguage(transcriptId, language)
                .map(existingSummary -> {
                    existingSummary.setSummary(content);
                    return existingSummary;
                })
                .orElseGet(() -> {
                    Summary newSummary = new Summary();
                    newSummary.setTranscriptId(transcriptId);
                    newSummary.setLanguage(language);
                    newSummary.setSummary(content);
                    return newSummary;
                });

        return summaryRepository.save(summary);
    }



    public SummaryResponseDTO getSummary(String summaryId) {
        Summary summary = summaryRepository.findById(summaryId).orElseThrow(() -> new SummaryNotFoundException("Summary not found by id: " + summaryId));
        return new SummaryResponseDTO(summary.getId(), summary.getSummary(), summary.getLanguage(), summary.getTranscriptId());
    }

    public List<SummaryResponseDTO> getAllSummaries() {
        List<Summary> summaryList = summaryRepository.findAll();
        if (summaryList.isEmpty()) {
            throw new SummaryNotFoundException("No summaries found.");
        }
        return summaryList.stream()
                .map(s -> new SummaryResponseDTO(s.getId(), s.getSummary(), s.getLanguage(), s.getTranscriptId()))
                .toList();
    }
}
