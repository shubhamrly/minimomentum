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

/** AI Generated Code - Test skeleton structure **/

@WebMvcTest(TranscriptionController.class)
class TranscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranscriptionService transcriptionService;

    private TranscriptResponseDTO mockTranscript(Long id, String language) {
        TranscriptResponseDTO transcriptResponseDto = new TranscriptResponseDTO();
        transcriptResponseDto.setTranscriptId(id);
        if(language.equals("Spanish")) {
            transcriptResponseDto.setTranscriptText("[00:00:01] Javier R (Agente de Ventas - Software CRM): Hola, soy Javier R de CRM Soluciones. ¿Cómo está hoy?  \n[00:00:05] Laura G (Cliente - Gerente Tienda ABC): Hola, Javier. Estoy bien, gracias.");
        } else {
            transcriptResponseDto.setTranscriptText("[00:00:01] Jenna L (Sales Agent - Tech Solutions): Hi, this is Jenna L from Tech Solutions. How are you doing today?\\n\\n[00:00:05] Mark T (Customer - Global Enterprises): Hi Jenna, I'm doing well, thanks. Just busy with the usual workload. What can I help you with?");
        }
        transcriptResponseDto.setLanguage(language);
        return transcriptResponseDto;
    }

    @Test
    void given_validTranscriptRequest_when_generateTranscript_then_returnSuccessfully() throws Exception {
        TranscriptResponseDTO mockTranscriptResponse = mockTranscript(1L, "English");

        Mockito.when(transcriptionService.generateTranscript(anyString())).thenReturn(mockTranscriptResponse);

        mockMvc.perform(post("/api/v2/transcriptions/transcripts")
                        .param("language", "English"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transcriptId").value(1))
                .andExpect(jsonPath("$.language").value("English"));
    }

    @Test
    void given_validTranscriptId_when_getTranscriptById_then_returnSuccessfully() throws Exception {
        TranscriptResponseDTO mockTranscriptResponse = mockTranscript(2L, "Spanish");

        Mockito.when(transcriptionService.getTranscriptDtoById(anyLong())).thenReturn(mockTranscriptResponse);

        mockMvc.perform(get("/api/v2/transcriptions/transcripts/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transcriptId").value(2))
                .andExpect(jsonPath("$.transcriptText").value("[00:00:01] Javier R (Agente de Ventas - Software CRM): Hola, soy Javier R de CRM Soluciones. ¿Cómo está hoy?  \n[00:00:05] Laura G (Cliente - Gerente Tienda ABC): Hola, Javier. Estoy bien, gracias."));
    }

    @Test
    void given_existingTranscripts_when_getAllTranscripts_then_returnSuccessfully() throws Exception {
        List<TranscriptResponseDTO> list = List.of(
                mockTranscript(1L, "English"),
                mockTranscript(2L, "Spanish")
        );

        Mockito.when(transcriptionService.getAllTranscripts()).thenReturn(list);

        mockMvc.perform(get("/api/v2/transcriptions/transcripts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].transcriptId").value(1))
                .andExpect(jsonPath("$[1].language").value("Spanish"));
    }
}
