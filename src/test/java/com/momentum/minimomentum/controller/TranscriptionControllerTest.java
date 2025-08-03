package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.service.TranscriptionService;
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

@WebMvcTest(TranscriptionController.class)
class TranscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    private TranscriptResponseDTO mockTranscript(Long id, String language) {
        TranscriptResponseDTO dto = new TranscriptResponseDTO();
        dto.setTranscriptId(id);
        dto.setLanguage(language);
        dto.setTranscriptText("This is a test transcript.");
        return dto;
    }

    @Test
    void shouldGenerateTranscriptSuccessfully() throws Exception {
        TranscriptResponseDTO mockResponse = mockTranscript(1L, "english");

        Mockito.when(transcriptionService.generateTranscript(anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v2/transcriptions/transcripts")
                        .param("language", "english"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transcriptId").value(1))
                .andExpect(jsonPath("$.language").value("english"));
    }

    @Test
    void shouldGetTranscriptByIdSuccessfully() throws Exception {
        TranscriptResponseDTO mockResponse = mockTranscript(2L, "english");

        Mockito.when(transcriptionService.getTranscriptDtoById(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v2/transcriptions/transcripts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transcriptId").value(2))
                .andExpect(jsonPath("$.transcriptText").value("This is a test transcript."));
    }

    @Test
    void shouldReturnAllTranscriptsSuccessfully() throws Exception {
        List<TranscriptResponseDTO> list = List.of(
                mockTranscript(1L, "english"),
                mockTranscript(2L, "french")
        );

        Mockito.when(transcriptionService.getAllTranscripts()).thenReturn(list);

        mockMvc.perform(get("/api/v2/transcriptions/transcripts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].transcriptId").value(1))
                .andExpect(jsonPath("$[1].language").value("french"));
    }
}
