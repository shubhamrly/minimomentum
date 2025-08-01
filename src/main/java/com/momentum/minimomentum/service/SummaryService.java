package com.momentum.minimomentum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.dto.responseDTO.SummaryDTO;
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
    private TranscriptionService generationService;
    @Autowired
    private OpenAiClient openAiClient;
    @Autowired
    private SummaryRepository summaryRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public SummaryResponseDTO generateSummary(Long transcriptId, String language) throws JsonProcessingException {
        Transcript transcript = generationService.getTranscriptById(transcriptId);
        String prompt = PromptUtils.getPrompt(PromptType.SUMMARY_PROMPT, language) + "\n\n" + transcript.getTranscriptText();
        String content = openAiClient.getCompletion(prompt);

        Summary summary = saveOrUpdateSummary(content, transcriptId, language);
        return convertToSummaryResponseDTO(summary);
    }

    private Summary saveOrUpdateSummary(String content, Long transcriptId, String language) throws JsonProcessingException {
        SummaryDTO wrapper = objectMapper.readValue(content, SummaryDTO.class);

        String summaryText = wrapper.getSummary();
        SummaryDetails summaryDetails = toSummaryDetailsEntity(wrapper.getSummaryDetails());

         Summary summaryObj = summaryRepository.findByTranscriptIdAndLanguage(transcriptId, language)
                .map(existing -> {
                    existing.setSummaryText(summaryText);
                    existing.setSummaryDetails(summaryDetails);
                    return existing;
                })
                .orElseGet(() -> {
                    Summary newSummary = new Summary();
                    Transcript transcript = generationService.getTranscriptById(transcriptId);
                    newSummary.setTranscript(transcript);
                    newSummary.setLanguage(language);
                    newSummary.setSummaryText(summaryText);
                    newSummary.setSummaryDetails(summaryDetails);
                    return newSummary;
                });
        return summaryRepository.save(summaryObj);
    }



    public SummaryResponseDTO getSummary(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new EntityNotFoundException("Summary not found by id: " + summaryId));
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

        SummaryDetailsDTO summaryDetailsDTO = toSummaryDetailsDto(summary.getSummaryDetails());

        return new SummaryResponseDTO(
                summary.getId(),
                summary.getSummaryText(),
                summaryDetailsDTO,
                summary.getTranscript().getId().toString(),
                summary.getLanguage()
        );

    }


    private SummaryDetailsDTO toSummaryDetailsDto(SummaryDetails entity) {
        if (entity == null) return null;

        SummaryDetailsDTO dto = new SummaryDetailsDTO();
        dto.setTone(entity.getTone());
        dto.setOutcome(entity.getOutcome());
        dto.setWhatWentWell(entity.getWhatWentWell());
        dto.setWhatCouldBeImproved(entity.getWhatCouldBeImproved());
        dto.setObjectionsOrDiscoveryInsights(entity.getObjectionsOrDiscoveryInsights());
        dto.setActionPoints(entity.getActionPoints());
        dto.setAgent(entity.getAgent());
        dto.setCustomer(entity.getCustomer());

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
        entity.setTone(dto.getTone());
        entity.setOutcome(dto.getOutcome());
        entity.setWhatWentWell(dto.getWhatWentWell());
        entity.setWhatCouldBeImproved(dto.getWhatCouldBeImproved());
        entity.setObjectionsOrDiscoveryInsights(dto.getObjectionsOrDiscoveryInsights());
        entity.setActionPoints(dto.getActionPoints());
        entity.setAgent(dto.getAgent());
        entity.setCustomer(dto.getCustomer());

        if (dto.getChurnRiskSignals() != null) {
            SummaryDetails.ChurnRiskSignals crs = new SummaryDetails.ChurnRiskSignals();
            crs.setRiskLevel(dto.getChurnRiskSignals().getRiskLevel());
            crs.setSignals(dto.getChurnRiskSignals().getSignals());
            entity.setChurnRiskSignals(crs);
        }

        return entity;
    }
}
