package com.momentum.minimomentum.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.minimomentum.constant.PromptConstants;

import com.momentum.minimomentum.dto.responseDTO.SummaryDTO;
import com.momentum.minimomentum.dto.responseDTO.SummaryDetailsDTO;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Summary;
import com.momentum.minimomentum.model.SummaryDetails;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.SummaryRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryService {

    private final TranscriptionService generationService;

    private final OpenAiClient openAiClient;

    private final SummaryRepository summaryRepository;

    private final ObjectMapper objectMapper;

    private final String className = getClass().getSimpleName();

    public SummaryResponseDTO generateSummary(Long transcriptId, String language) throws JsonProcessingException {
        Transcript transcript = generationService.getTranscriptById(transcriptId);

        String summaryPrompt = PromptConstants.SUMMARY_PROMPT_CONSTANT
                .replace("%s", language)
                .concat(transcript.getTranscriptText())
                .replaceAll("\\s+", " ").trim();

        String content = openAiClient.getCompletionOpenAi(summaryPrompt);

        Summary summary = saveOrUpdateSummary(content, transcriptId, language);
        log.info("[{}] Generated summary for transcriptId: {}, language: {}", className, transcriptId, language);
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
        log.info("[{}] Saving summary for transcriptId: {}, language: {}", className, transcriptId, language);
        return summaryRepository.save(summaryObj);
    }

    public SummaryResponseDTO getSummary(Long summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new EntityNotFoundException("Summary not found by id: " + summaryId));
        log.info("[{}] Fetched summary with id: {}", className, summaryId);
        return convertToSummaryResponseDTO(summary);
    }

    public List<SummaryResponseDTO> getAllSummaries() {
        List<Summary> summaryList = summaryRepository.findAll();
        if (summaryList.isEmpty()) {
            throw new EntityNotFoundException("No summaries found.");
        }
        log.info("[{}] Fetched {} summaries", className, summaryList.size());
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
        if (entity == null) {
            return null;
        }

        SummaryDetailsDTO summaryDetailsDto = new SummaryDetailsDTO();
        summaryDetailsDto.setTone(entity.getTone());
        summaryDetailsDto.setOutcome(entity.getOutcome());
        summaryDetailsDto.setWhatWentWell(entity.getWhatWentWell());
        summaryDetailsDto.setWhatCouldBeImproved(entity.getWhatCouldBeImproved());
        summaryDetailsDto.setObjectionsOrDiscoveryInsights(entity.getObjectionsOrDiscoveryInsights());
        summaryDetailsDto.setActionPoints(entity.getActionPoints());
        summaryDetailsDto.setAgent(entity.getAgent());
        summaryDetailsDto.setCustomer(entity.getCustomer());

        if (entity.getChurnRiskSignals() != null) {
            SummaryDetailsDTO.ChurnRiskSignalsDTO crsDto = new SummaryDetailsDTO.ChurnRiskSignalsDTO();
            crsDto.setRiskLevel(entity.getChurnRiskSignals().getRiskLevel());
            crsDto.setSignals(entity.getChurnRiskSignals().getSignals());
            summaryDetailsDto.setChurnRiskSignals(crsDto);
        }

        return summaryDetailsDto;
    }

    private SummaryDetails toSummaryDetailsEntity(SummaryDetailsDTO dto) {
        if (dto == null) {
            return null;
        }

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
