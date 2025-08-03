package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.service.QuestionAnswerService;
import com.momentum.minimomentum.service.TranscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** AI Generated Code - Test skeleton structure **/


@WebMvcTest(TranscriptQAController.class)
class TranscriptQAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    @MockBean
    private QuestionAnswerService questionAnswerService;

    @Test
    void given_validTranscriptId_when_getAnswer_then_returns200() throws Exception {
        // Mock response
        TranscriptQAResponseDTO mockQAResponse = new TranscriptQAResponseDTO();
        mockQAResponse.setAnswer("The customer, Mr. Johnson, expressed interest in the Inventory Management System offered by RetailTech Solutions.");

        when(questionAnswerService.getAnswersByTranscriptId(eq(1L), any()))
                .thenReturn("The customer, Mr. Johnson, expressed interest in the Inventory Management System offered by RetailTech Solutions.");

        mockMvc.perform(post("/api/v2/transcriptions/transcript/1/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("What did customer say about sales product?"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("The customer, Mr. Johnson, expressed interest in the Inventory Management System offered by RetailTech Solutions."));

    }

    @Test
    void given_invalidTranscriptId_when_getAnswer_then_returns404() throws Exception {
        when(questionAnswerService.getAnswersByTranscriptId(eq(0L), any()))
                .thenThrow(new EntityNotFoundException("Transcript not found by id: 0"));

        when(transcriptionService.getTranscriptById(0L))
                .thenThrow(new EntityNotFoundException("Transcript not found by id: 0"));

        mockMvc.perform(post("/api/v2/transcriptions/transcript/0/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("What is CRM?"))
                .andExpect(status().isNotFound());
    }

    @Test
    void given_validTranscriptId_when_getAllAnswers_then_returns200() throws Exception {
        TranscriptQAResponseDTO mockQAResponse = new TranscriptQAResponseDTO();
        mockQAResponse.setAnswer("The customer, Mr. Johnson, expressed interest in the Inventory Management System");

        List<TranscriptQAResponseDTO> list = List.of(mockQAResponse);

        when(questionAnswerService.getAllQAByTranscriptId(1L)).thenReturn(list);

        mockMvc.perform(get("/api/v2/transcriptions/transcript/1/answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].answer").value("The customer, Mr. Johnson, expressed interest in the Inventory Management System"));
    }

    @Test
    void given_invalidTranscriptId_when_getAllAnswers_then_returns404() throws Exception {
        when(questionAnswerService.getAllQAByTranscriptId(0L))
                .thenThrow(new EntityNotFoundException("Transcript not found"));
        when(transcriptionService.getTranscriptById(0L))
                .thenThrow(new EntityNotFoundException("Transcript not found by id: 0"));

        mockMvc.perform(get("/api/v2/transcriptions/transcript/0/answers"))
                .andExpect(status().isNotFound());
    }
}
