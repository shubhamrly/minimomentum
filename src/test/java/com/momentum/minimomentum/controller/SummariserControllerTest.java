package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.SummaryDetailsDTO;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.service.SummaryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AI Generated Code - Test skeleton structure *
 */
@WebMvcTest(SummariserController.class)
class SummariserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SummaryService summaryService;

    private SummaryResponseDTO createMockResponse(Long id, String lang) {

        SummaryDetailsDTO summaryDetailsDto = getSummaryDetailsDTO();

        return new SummaryResponseDTO(id,
                "During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs, budget justification, and data security.",
                summaryDetailsDto, String.valueOf(id), lang);
    }

    private static SummaryDetailsDTO getSummaryDetailsDTO() {

        SummaryDetailsDTO.ChurnRiskSignalsDTO churnRisk = new SummaryDetailsDTO.ChurnRiskSignalsDTO();
        churnRisk.setRiskLevel("5");
        churnRisk.setSignals(List.of("Budget constraints", "Competitor interest"));

        SummaryDetailsDTO details = new SummaryDetailsDTO();
        details.setTone("Professional and informative");
        details.setOutcome("Customer interested in a demo");
        details.setStrengths(List.of("User-friendly system"));
        details.setImprovements(List.of("Follow-up time"));
        details.setInsights(List.of("Concern about complexity"));
        details.setChurnRiskSignals(churnRisk);
        details.setActionPoints(List.of("Send detailed quote"));
        details.setAgent("Sarah L");
        details.setCustomer("Mr. Johnson, Trendy Apparel");
        return details;
    }

    @Test
    void given_validTranscriptId_and_language_when_generateSummary_then_return_summaryResponse() throws Exception {
        SummaryResponseDTO response = createMockResponse(1L, "English");

        Mockito.when(summaryService.generateSummary(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v2/summariser/summaries")
                .param("transcriptId", "1")
                .param("language", "English"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(1))
                .andExpect(jsonPath("$.summaryText").value("During the call, Jenna L from Tech Solutions engaged Mark T from Global Enterprises about their AI-driven HR software. Mark expressed interest yet raised concerns about switching costs, budget justification, and data security."))
                .andExpect(jsonPath("$.summaryDetails.tone").value("Professional and informative"));
    }

    @Test
    void given_validSummaryId_when_getSummary_then_return_summaryResponse() throws Exception {
        SummaryResponseDTO response = createMockResponse(2L, "English");

        Mockito.when(summaryService.getSummary(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v2/summariser/summaries/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(2))
                .andExpect(jsonPath("$.summaryDetails.tone").value("Professional and informative"));
    }

    @Test
    void given_invalidSummaryId_when_getSummary_then_return_404NotFound() throws Exception {
        Mockito.when(summaryService.getSummary(999L)).thenThrow(new EntityNotFoundException("Summary not found by id: 999"));

        mockMvc.perform(get("/api/v2/summariser/summaries/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Summary not found by id: 999"));
    }

    @Test
    void given_summariesExist_when_getAllSummaries_then_returnListSuccessfully() throws Exception {
        List<SummaryResponseDTO> responses = List.of(
                createMockResponse(1L, "English"),
                createMockResponse(2L, "Spanish")
        );

        Mockito.when(summaryService.getAllSummaries()).thenReturn(responses);

        mockMvc.perform(get("/api/v2/summariser/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[1].language").value("Spanish"));
    }
}
