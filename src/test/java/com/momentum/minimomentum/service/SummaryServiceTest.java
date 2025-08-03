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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class SummaryServiceTest {

    @InjectMocks
    private SummaryService summaryService;

    @Mock
    private TranscriptionService generationService;

    @Mock
    private OpenAiClient openAiClient;

    @Mock
    private SummaryRepository summaryRepository;

    @Mock
    private ObjectMapper objectMapper;

    private static String getMockContent() {
        final String summaryText = "During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs, budget justification, and data security.";
        String tone = "Professional and informative";

        return """
                {
                  "summary": "%s",
                  "summaryDetails": {
                    "tone": "%s"
                  }
                }
                """.formatted(summaryText, tone);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenTranscriptExists_whenGenerateSummary_thenReturnSummaryResponseDTO() throws JsonProcessingException {

        Long transcriptId = 1L;
        String language = "English";

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);
        transcript.setTranscriptText("[00:00:01] Javier R (Agente de Ventas - Software CRM): Hola, soy Javier R de CRM Soluciones. ¿Cómo está hoy?  \n[00:00:05] Laura G (Cliente - Gerente Tienda ABC): Hola, Javier. Estoy bien, gracias.");

        String expectedPrompt = PromptConstants.SUMMARY_PROMPT_CONSTANT.replace("%s", language).concat(transcript.getTranscriptText()).replaceAll("\\s+", " ").trim();

        String expectedSummaryText = "During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs, budget justification, and data security.";
        String expectedTone = "Professional and informative";

        String mockContent = getMockContent();

        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setSummary(expectedSummaryText); // ✅ Correct value
        SummaryDetailsDTO detailsDTO = new SummaryDetailsDTO();
        detailsDTO.setTone(expectedTone);
        summaryDTO.setSummaryDetails(detailsDTO);

        when(generationService.getTranscriptById(transcriptId)).thenReturn(transcript);
        when(openAiClient.getCompletionOpenAi(expectedPrompt)).thenReturn(mockContent);
        when(objectMapper.readValue(mockContent, SummaryDTO.class)).thenReturn(summaryDTO);
        when(summaryRepository.findByTranscriptIdAndLanguage(transcriptId, language)).thenReturn(Optional.empty());
        when(summaryRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        SummaryResponseDTO response = summaryService.generateSummary(transcriptId, language);

        assertEquals(expectedSummaryText, response.getSummaryText());
        assertEquals(expectedTone, response.getSummaryDetails().getTone());
        assertEquals(transcriptId.toString(), response.getTranscriptId());
        assertEquals(language, response.getLanguage());
    }

    @Test
    void givenTranscriptIdAndLanguage_whenGetSummary_thenReturnSummaryResponseDTO() {
        Summary summary = getSummary();

        when(summaryRepository.findById(1L)).thenReturn(Optional.of(summary));

        SummaryResponseDTO summaryResponseDto = summaryService.getSummary(1L);

        assertEquals("During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs.", summaryResponseDto.getSummaryText());
        assertEquals("Professional and informative", summaryResponseDto.getSummaryDetails().getTone());
        assertEquals("English", summaryResponseDto.getLanguage());
        assertEquals("10", summaryResponseDto.getTranscriptId());
    }

    private static Summary getSummary() {
        Summary summary = new Summary();
        summary.setId(1L);
        summary.setSummaryText("During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs.");
        summary.setLanguage("English");

        Transcript transcript = new Transcript();
        transcript.setId(10L);
        summary.setTranscript(transcript);

        SummaryDetails details = new SummaryDetails();
        details.setTone("Professional and informative");
        summary.setSummaryDetails(details);
        return summary;
    }

    @Test
    void givenTranscriptIdAndLanguage_whenGetSummary_thenThrowSummaryNotFoundException() {
        when(summaryRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> summaryService.getSummary(101L));
    }

    @Test
    void givenTranscriptId_whenGetAllSummaries_thenThrowSummaryNotFoundException() {
        when(summaryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> summaryService.getAllSummaries());
    }

    @Test
    void givenTranscriptId_whenGetAllSummaries_thenReturnListOfSummaryResponseDTO() {
        Summary summary = getSummary();
        when(summaryRepository.findAll()).thenReturn(List.of(summary));

        List<SummaryResponseDTO> list = summaryService.getAllSummaries();

        assertEquals(1, list.size());
        assertEquals("During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs.", list.get(0).getSummaryText());
    }
}
