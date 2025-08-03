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
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateSummary_whenTranscriptExists_thenReturnSummaryResponseDTO() throws JsonProcessingException {
        // Arrange
        Long transcriptId = 1L;
        String language = "English";

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);
        transcript.setTranscriptText("This is a sample transcript.");

        String expectedPrompt = PromptConstants.SUMMARY_PROMPT_CONSTANT
                .replace("%s", language)
                .concat(transcript.getTranscriptText())
                .replaceAll("\\s+", " ").trim();

        String aiResponse = "{\"summary\": \"This is the summary.\", \"summaryDetails\": {\"tone\": \"Positive\"}}";

        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setSummary("This is the summary.");
        SummaryDetailsDTO detailsDTO = new SummaryDetailsDTO();
        detailsDTO.setTone("Positive");
        summaryDTO.setSummaryDetails(detailsDTO);

        when(generationService.getTranscriptById(transcriptId)).thenReturn(transcript);
        when(openAiClient.getCompletionOpenAi(expectedPrompt)).thenReturn(aiResponse);
        when(objectMapper.readValue(aiResponse, SummaryDTO.class)).thenReturn(summaryDTO);
        when(summaryRepository.findByTranscriptIdAndLanguage(transcriptId, language)).thenReturn(Optional.empty());
        when(summaryRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        SummaryResponseDTO response = summaryService.generateSummary(transcriptId, language);

        // Assert
        assertEquals("This is the summary.", response.getSummaryText());
        assertEquals("Positive", response.getSummaryDetails().getTone());
        assertEquals(transcriptId.toString(), response.getTranscriptId());
        assertEquals(language, response.getLanguage());
    }

    @Test
    void testGetSummary_whenExists_thenReturnDTO() {
        Summary summary = new Summary();
        summary.setId(1L);
        summary.setSummaryText("Hello");
        summary.setLanguage("English");

        Transcript transcript = new Transcript();
        transcript.setId(10L);
        summary.setTranscript(transcript);

        SummaryDetails details = new SummaryDetails();
        details.setTone("Neutral");
        summary.setSummaryDetails(details);

        when(summaryRepository.findById(1L)).thenReturn(Optional.of(summary));

        SummaryResponseDTO dto = summaryService.getSummary(1L);

        assertEquals("Hello", dto.getSummaryText());
        assertEquals("Neutral", dto.getSummaryDetails().getTone());
        assertEquals("English", dto.getLanguage());
        assertEquals("10", dto.getTranscriptId());
    }

    @Test
    void testGetSummary_whenNotFound_thenThrowException() {
        when(summaryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> summaryService.getSummary(99L));
    }

    @Test
    void testGetAllSummaries_whenNone_thenThrowException() {
        when(summaryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> summaryService.getAllSummaries());
    }

    @Test
    void testGetAllSummaries_whenExist_thenReturnList() {
        Summary summary = new Summary();
        summary.setId(1L);
        summary.setLanguage("English");
        summary.setSummaryText("Summary");
        SummaryDetails details = new SummaryDetails();
        details.setTone("Excited");
        summary.setSummaryDetails(details);
        Transcript transcript = new Transcript();
        transcript.setId(5L);
        summary.setTranscript(transcript);

        when(summaryRepository.findAll()).thenReturn(List.of(summary));

        List<SummaryResponseDTO> list = summaryService.getAllSummaries();

        assertEquals(1, list.size());
        assertEquals("Summary", list.get(0).getSummaryText());
    }
}
