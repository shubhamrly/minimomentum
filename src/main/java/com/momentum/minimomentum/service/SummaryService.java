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

/**
 * SummaryService is responsible for generating and managing summaries of transcripts.
 * It interacts with the OpenAI API to generate summaries based on the transcript text
 * and provides methods to retrieve, save, and update summaries.
 */


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

    private SummaryDetailsDTO toSummaryDetailsDto(SummaryDetails summaryDetailsEntity) {
        if (summaryDetailsEntity == null) {
            return null;
        }

        SummaryDetailsDTO summaryDetailsDto = new SummaryDetailsDTO();
        summaryDetailsDto.setTone(summaryDetailsEntity.getTone());
        summaryDetailsDto.setOutcome(summaryDetailsEntity.getOutcome());
        summaryDetailsDto.setStrengths(summaryDetailsEntity.getStrengths());
        summaryDetailsDto.setImprovements(summaryDetailsEntity.getImprovements());
        summaryDetailsDto.setInsights(summaryDetailsEntity.getInsights());
        summaryDetailsDto.setActionPoints(summaryDetailsEntity.getActionPoints());
        summaryDetailsDto.setAgent(summaryDetailsEntity.getAgent());
        summaryDetailsDto.setCustomer(summaryDetailsEntity.getCustomer());

        if (summaryDetailsEntity.getChurnRiskSignals() != null) {
            SummaryDetailsDTO.ChurnRiskSignalsDTO crsDto = new SummaryDetailsDTO.ChurnRiskSignalsDTO();
            crsDto.setRiskLevel(summaryDetailsEntity.getChurnRiskSignals().getRiskLevel());
            crsDto.setSignals(summaryDetailsEntity.getChurnRiskSignals().getSignals());
            summaryDetailsDto.setChurnRiskSignals(crsDto);
        }

        return summaryDetailsDto;
    }

    private SummaryDetails toSummaryDetailsEntity(SummaryDetailsDTO summaryDetailsDto) {
        if (summaryDetailsDto == null) {
            return null;
        }

        SummaryDetails summaryDetailsEntity = new SummaryDetails();
        summaryDetailsEntity.setTone(summaryDetailsDto.getTone());
        summaryDetailsEntity.setOutcome(summaryDetailsDto.getOutcome());
        summaryDetailsEntity.setStrengths(summaryDetailsDto.getStrengths());
        summaryDetailsEntity.setImprovements(summaryDetailsDto.getImprovements());
        summaryDetailsEntity.setInsights(summaryDetailsDto.getInsights());
        summaryDetailsEntity.setActionPoints(summaryDetailsDto.getActionPoints());
        summaryDetailsEntity.setAgent(summaryDetailsDto.getAgent());
        summaryDetailsEntity.setCustomer(summaryDetailsDto.getCustomer());

        if (summaryDetailsDto.getChurnRiskSignals() != null) {
            SummaryDetails.ChurnRiskSignals crs = new SummaryDetails.ChurnRiskSignals();
            crs.setRiskLevel(summaryDetailsDto.getChurnRiskSignals().getRiskLevel());
            crs.setSignals(summaryDetailsDto.getChurnRiskSignals().getSignals());
            summaryDetailsEntity.setChurnRiskSignals(crs);
        }

        return summaryDetailsEntity;
    }
}
