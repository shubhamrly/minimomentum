package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.TranscriptionRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TranscriptionServiceTest {

    @Mock
    private OpenAiClient openAiClient;

    @Mock
    private TranscriptionRepository transcriptionRepository;

    @InjectMocks
    private TranscriptionService transcriptionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidTranscriptRequest_whenGenerateTranscript_thenReturnsTranscriptResponse() {
        // given
        String language = "English";
        String mockPrompt = PromptConstants.GENERATION_PROMPT_CONSTANT.replace("%s", language).replaceAll("\\s+", " ").trim();
        String mockContent = "[00:00:01] Jenna L (Sales Agent - Tech Solutions): Hi, this is Jenna L from Tech Solutions. How are you doing today?\\n\\n[00:00:05] Mark T (Customer - Global Enterprises): Hi Jenna, I'm doing well";
        Transcript transcript = getMockTranscript(mockContent, language);

        when(openAiClient.getCompletionOpenAi(anyString())).thenReturn(mockContent);
        when(transcriptionRepository.save(any(Transcript.class))).thenReturn(transcript);

        TranscriptResponseDTO transcriptResponseDTO = transcriptionService.generateTranscript(language);

        assertNotNull(transcriptResponseDTO);
        assertEquals(language, transcriptResponseDTO.getLanguage());
        assertEquals(mockContent, transcriptResponseDTO.getTranscriptText());
    }

    @Test
    void givenTranscriptExists_whenGetById_thenReturnTranscript() {
        Transcript transcript = getMockTranscript("[00:00:01] Jenna L (Sales Agent - Tech Solutions): Hi, this is Jenna L from Tech Solutions. How are you doing today?\\n\\n[00:00:05] Mark T (Customer - Global Enterprises): Hi Jenna, I'm doing well", "english");

        when(transcriptionRepository.findById(1L)).thenReturn(Optional.of(transcript));

        Transcript result = transcriptionService.getTranscriptById(1L);
        assertNotNull(result);

        assertEquals("english", result.getLanguage());
    }

    @Test
    void givenTranscriptNotExist_whenGetById_thenThrowException() {
        when(transcriptionRepository.findById(0L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transcriptionService.getTranscriptById(0L));

        assertEquals("Transcript not found by id: 0", exception.getMessage());
    }

    @Test
    void givenTranscriptsExist_whenGetAll_thenReturnList() {
        List<Transcript> transcripts = List.of(
                getMockTranscript("""
        [00:00:01] Jenna L (Sales Agent - Tech Solutions): Hi, this is Jenna L from Tech Solutions. How are you doing today?

        [00:00:05] Mark T (Customer - Global Enterprises): Hi Jenna, I'm doing well
        """, "english"),
                getMockTranscript("""
        [00:00:01] Javier R (Agente de Ventas - Software CRM): Hola, soy Javier R de CRM Soluciones. ¿Cómo está hoy?  

        [00:00:05] Laura G (Cliente - Gerente Tienda ABC): Hola, Javier.
        """, "spanish")
        );

        when(transcriptionRepository.findAll()).thenReturn(transcripts);

        List<TranscriptResponseDTO> result = transcriptionService.getAllTranscripts();

        assertEquals(2, result.size());
    }

    @Test
    void givenNoTranscriptsExist_whenGetAll_thenThrowException() {
        when(transcriptionRepository.findAll()).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transcriptionService.getAllTranscripts());

        assertEquals("No transcripts found.", exception.getMessage());
    }

    private Transcript getMockTranscript(String text, String language) {
        Transcript transcript = new Transcript();
        transcript.setId(1L);
        transcript.setLanguage(language);
        transcript.setTranscriptText(text);
        transcript.setCreateDateTime(LocalDateTime.now());
        return transcript;
    }
}
