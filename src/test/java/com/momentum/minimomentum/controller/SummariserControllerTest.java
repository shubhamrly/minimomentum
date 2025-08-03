package com.momentum.minimomentum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.momentum.minimomentum.dto.responseDTO.SummaryDetailsDTO;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.service.SummaryService;
import lombok.RequiredArgsConstructor;
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

@WebMvcTest(SummariserController.class)
class SummariserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  SummaryService summaryService;

    private SummaryResponseDTO createMockResponse(Long id, String lang) {
        SummaryDetailsDTO.ChurnRiskSignalsDTO churnRisk = new SummaryDetailsDTO.ChurnRiskSignalsDTO();
        churnRisk.setRiskLevel("Medium");
        churnRisk.setSignals(List.of("Late response"));

        SummaryDetailsDTO details = new SummaryDetailsDTO();
        details.setTone("Positive");
        details.setOutcome("Successful");
        details.setStrengths(List.of("Clear communication"));
        details.setImprovements(List.of("Follow-up time"));
        details.setInsights(List.of("Pricing"));
        details.setChurnRiskSignals(churnRisk);
        details.setActionPoints(List.of("Send proposal"));
        details.setAgent("Alice");
        details.setCustomer("Bob");

        return new SummaryResponseDTO(id, "summary text " + id, details, String.valueOf(id), lang);
    }

    @Test
    void shouldGenerateSummarySuccessfully() throws Exception {
        SummaryResponseDTO response = createMockResponse(1L, "english");

        Mockito.when(summaryService.generateSummary(anyLong(), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v2/summariser/summaries")
                        .param("transcriptId", "1")
                        .param("language", "english"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(1))
                .andExpect(jsonPath("$.summaryText").value("summary text 1"))
                .andExpect(jsonPath("$.summaryDetails.tone").value("Positive"));
    }

    @Test
    void shouldFetchSummaryByIdSuccessfully() throws Exception {
        SummaryResponseDTO response = createMockResponse(2L, "english");

        Mockito.when(summaryService.getSummary(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v2/summariser/summaries/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summaryId").value(2))
                .andExpect(jsonPath("$.summaryDetails.tone").value("Positive"));
    }

    @Test
    void shouldReturn404WhenSummaryNotFound() throws Exception {
        Mockito.when(summaryService.getSummary(999L)).thenThrow(new EntityNotFoundException("Summary not found"));

        mockMvc.perform(get("/api/v2/summariser/summaries/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Summary not found"));
    }

    @Test
    void shouldFetchAllSummariesSuccessfully() throws Exception {
        List<SummaryResponseDTO> responses = List.of(
                createMockResponse(1L, "english"),
                createMockResponse(2L, "spanish")
        );

        Mockito.when(summaryService.getAllSummaries()).thenReturn(responses);

        mockMvc.perform(get("/api/v2/summariser/summaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].summaryId").value(1))
                .andExpect(jsonPath("$[1].language").value("spanish"));
    }
}
