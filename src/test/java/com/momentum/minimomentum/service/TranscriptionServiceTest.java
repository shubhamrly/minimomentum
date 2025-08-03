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

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTranscript_success() {
        // given
        String language = "English";
        String mockPrompt = PromptConstants.GENERATION_PROMPT_CONSTANT.replace("%s", language).replaceAll("\\s+", " ").trim();
        String mockContent = "Sample transcript content";
        Transcript transcript = getMockTranscript(mockContent, language);

        when(openAiClient.getCompletionOpenAi(anyString())).thenReturn(mockContent);
        when(transcriptionRepository.save(any(Transcript.class))).thenReturn(transcript);

        // when
        TranscriptResponseDTO result = transcriptionService.generateTranscript(language);

        // then
        assertNotNull(result);
        assertEquals(language, result.getLanguage());
        assertEquals(mockContent, result.getTranscriptText());
    }

    @Test
    void testGetTranscriptById_success() {
        Transcript transcript = getMockTranscript("Hello", "en");
        when(transcriptionRepository.findById(1L)).thenReturn(Optional.of(transcript));

        Transcript result = transcriptionService.getTranscriptById(1L);

        assertNotNull(result);
        assertEquals("en", result.getLanguage());
    }

    @Test
    void testGetTranscriptById_notFound() {
        when(transcriptionRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transcriptionService.getTranscriptById(1L));

        assertEquals("Transcript not found by id: 1", exception.getMessage());
    }

    @Test
    void testGetAllTranscripts_success() {
        List<Transcript> transcripts = List.of(getMockTranscript("t1", "en"), getMockTranscript("t2", "fr"));

        when(transcriptionRepository.findAll()).thenReturn(transcripts);

        List<TranscriptResponseDTO> result = transcriptionService.getAllTranscripts();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllTranscripts_empty() {
        when(transcriptionRepository.findAll()).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transcriptionService.getAllTranscripts());

        assertEquals("No transcripts found.", exception.getMessage());
    }

    private Transcript getMockTranscript(String text, String lang) {
        Transcript transcript = new Transcript();
        transcript.setId(1L);
        transcript.setLanguage(lang);
        transcript.setTranscriptText(text);
        transcript.setCreateDateTime(LocalDateTime.now());
        return transcript;
    }
}
