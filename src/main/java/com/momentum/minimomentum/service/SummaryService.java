package com.momentum.minimomentum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.responseDTO.SummaryDetailsDTO;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Summary;
import com.momentum.minimomentum.model.SummaryDetails;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.SummaryRepository;
import com.momentum.minimomentum.service.openAi.OpenAiClient;
import com.momentum.minimomentum.utils.PromptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummaryService {
    @Autowired
    TranscriptionService generationService;
    @Autowired
    private OpenAiClient openAiClient;
    @Autowired
    private SummaryRepository summaryRepository;
    @Autowired
    private ObjectMapper objectMapper;


    public SummaryResponseDTO generateSummary(String transcriptId, String language) throws JsonProcessingException {
        Transcript transcript = generationService.getTranscriptById(Long.valueOf(transcriptId));
        String prompt = PromptUtils.getPrompt(PromptType.SUMMARY_PROMPT, language) + " \n\n " + transcript.getTranscriptText();
        String content = openAiClient.getCompletion(prompt);


        Summary summary = saveOrUpdateSummary(content, transcriptId, language);

        return convertToSummaryResponseDTO(summary);
    }

    private Summary saveOrUpdateSummary(String content, String transcriptId, String language) throws JsonProcessingException {
        SummaryDetailsDTO summaryDetailsDTO = objectMapper.readValue(content, SummaryDetailsDTO.class);

        SummaryDetails summaryDetails = toSummaryDetailsEntity(summaryDetailsDTO);
        Summary summary = summaryRepository.findByTranscriptIdAndLanguage(Long.valueOf(transcriptId), language)
                .map(existingSummary -> {
                    existingSummary.setSummary(summaryDetails);
                    return existingSummary;
                })
                .orElseGet(() -> {
                    Summary newSummary = new Summary();
                    Transcript transcript = generationService.getTranscriptById(Long.valueOf(transcriptId)); // Add this method in service
                    newSummary.setTranscript(transcript);
                    newSummary.setLanguage(language);
                    newSummary.setSummary(summaryDetails);
                    return newSummary;
                });

        return summaryRepository.save(summary);
    }


    public SummaryResponseDTO getSummary(String summaryId) {
        Summary summary = summaryRepository.findById(Long.valueOf(summaryId)).orElseThrow(() -> new EntityNotFoundException("Summary not found by id: " + summaryId));
        return convertToSummaryResponseDTO(summary);
    }

    public List<SummaryResponseDTO> getAllSummaries() {
        List<Summary> summaryList = summaryRepository.findAll();
        if (summaryList.isEmpty()) {
            throw new EntityNotFoundException("No summaries found.");
        }
        return summaryList.stream()
                .map(this::convertToSummaryResponseDTO)
                .toList();
    }

    private SummaryResponseDTO convertToSummaryResponseDTO(Summary summary) {
        SummaryDetailsDTO summaryDTO = toSummaryDetailsDto(summary.getSummary());
        return new SummaryResponseDTO(
                summary.getId(),
                summaryDTO,
                summary.getTranscript().getId().toString(),
                summary.getLanguage()
        );
    }

    private SummaryDetailsDTO toSummaryDetailsDto(SummaryDetails entity) {
        if (entity == null) return null;

        SummaryDetailsDTO dto = new SummaryDetailsDTO();
        dto.setSummary(entity.getSummary());
        dto.setTone(entity.getTone());
        dto.setOutcome(entity.getOutcome());
        dto.setWhatWentWell(entity.getWhatWentWell());
        dto.setWhatCouldBeImproved(entity.getWhatCouldBeImproved());
        dto.setObjectionsOrDiscoveryInsights(entity.getObjectionsOrDiscoveryInsights());
        dto.setActionPoints(entity.getActionPoints());

        if (entity.getChurnRiskSignals() != null) {
            SummaryDetailsDTO.ChurnRiskSignalsDTO crsDto = new SummaryDetailsDTO.ChurnRiskSignalsDTO();
            crsDto.setRiskLevel(entity.getChurnRiskSignals().getRiskLevel());
            crsDto.setSignals(entity.getChurnRiskSignals().getSignals());
            dto.setChurnRiskSignals(crsDto);
        }

        return dto;
    }

    private SummaryDetails toSummaryDetailsEntity(SummaryDetailsDTO dto) {
        if (dto == null) return null;

        SummaryDetails entity = new SummaryDetails();
        entity.setSummary(dto.getSummary());
        entity.setTone(dto.getTone());
        entity.setOutcome(dto.getOutcome());
        entity.setWhatWentWell(dto.getWhatWentWell());
        entity.setWhatCouldBeImproved(dto.getWhatCouldBeImproved());
        entity.setObjectionsOrDiscoveryInsights(dto.getObjectionsOrDiscoveryInsights());
        entity.setActionPoints(dto.getActionPoints());

        if (dto.getChurnRiskSignals() != null) {
            SummaryDetails.ChurnRiskSignals crs = new SummaryDetails.ChurnRiskSignals();
            crs.setRiskLevel(dto.getChurnRiskSignals().getRiskLevel());
            crs.setSignals(dto.getChurnRiskSignals().getSignals());
            entity.setChurnRiskSignals(crs);
        }

        return entity;
    }




}
