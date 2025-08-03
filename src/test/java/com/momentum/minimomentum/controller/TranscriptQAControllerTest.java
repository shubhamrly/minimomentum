package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.service.QuestionAnswerService;
import com.momentum.minimomentum.service.TranscriptionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranscriptQAController.class)
class TranscriptQAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    @MockBean
    private QuestionAnswerService questionAnswerService;

    @Test
    void testGetAnswerByTranscriptId_Returns200() throws Exception {
        // Mock response
        TranscriptQAResponseDTO mockResponse = new TranscriptQAResponseDTO();
        mockResponse.setAnswer("This is a mock answer");

        when(questionAnswerService.getAnswersByTranscriptId(eq(1L), any()))
                .thenReturn("This is a mock answer");

        mockMvc.perform(post("/api/v2/transcriptions/transcript/1/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("What is CRM?"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("This is a mock answer"));
    }

    @Test
    void testGetAnswerByTranscriptId_Returns404() throws Exception {
        when(questionAnswerService.getAnswersByTranscriptId(eq(0L), any()))
                .thenThrow(new EntityNotFoundException("Transcript not found"));

        when(transcriptionService.getTranscriptById(0L))
                .thenThrow(new EntityNotFoundException("Transcript not found by id: 0"));

        mockMvc.perform(post("/api/v2/transcriptions/transcript/0/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("What is CRM?"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllAnswersByTranscriptId_Returns200() throws Exception {
        TranscriptQAResponseDTO dto = new TranscriptQAResponseDTO();
        dto.setAnswer("Answer 1");
        List<TranscriptQAResponseDTO> list = List.of(dto);

        when(questionAnswerService.getAllQAByTranscriptId(1L)).thenReturn(list);

        mockMvc.perform(get("/api/v2/transcriptions/transcript/1/answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].answer").value("Answer 1"));
    }

    @Test
    void testGetAllAnswersByTranscriptId_Returns404() throws Exception {
        when(questionAnswerService.getAllQAByTranscriptId(1L))
                .thenThrow(new EntityNotFoundException("Transcript not found"));
        when(transcriptionService.getTranscriptById(1L))
                .thenThrow(new EntityNotFoundException("Transcript not found by id: 1"));

        mockMvc.perform(get("/api/v2/transcriptions/transcript/1/answers"))
                .andExpect(status().isNotFound());
    }
}
