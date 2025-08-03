package com.momentum.minimomentum.service;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.model.QuestionAnswer;
import com.momentum.minimomentum.model.Transcript;
import com.momentum.minimomentum.repository.QuestionAnswersRepository;
import com.momentum.minimomentum.service.openAiService.OpenAiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionAnswerServiceTest {

    @Mock
    private TranscriptionService transcriptionService;

    @Mock
    private QuestionAnswersRepository questionAnswersRepository;

    @Mock
    private OpenAiClient openAiClient;

    @InjectMocks
    private QuestionAnswerService questionAnswerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAnswersByTranscriptId_shouldReturnGeneratedAnswerAndSaveIt() {
        Long transcriptId = 1L;
        String question = "What is the price?";
        String transcriptText = "This is a sales transcript.";
        String aiAnswer = "The price is $99";

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);
        transcript.setTranscriptText(transcriptText);

        QuestionAnswer savedQA = new QuestionAnswer();
        savedQA.setId(1L);
        savedQA.setTranscript(transcript);
        savedQA.setQuestion(question);
        savedQA.setAnswer(aiAnswer);
        savedQA.setCreateDateTime(LocalDateTime.now());

        when(transcriptionService.getTranscriptById(transcriptId)).thenReturn(transcript);
        when(openAiClient.getCompletionOpenAi(anyString())).thenReturn(aiAnswer);
        when(questionAnswersRepository.save(any(QuestionAnswer.class))).thenReturn(savedQA);

        String result = questionAnswerService.getAnswersByTranscriptId(transcriptId, question);

        assertEquals(aiAnswer, result);
        verify(questionAnswersRepository, times(1)).save(any());
    }

    @Test
    void getAllQAByTranscriptId_shouldReturnList_whenFound() {
        Long transcriptId = 1L;

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);

        QuestionAnswer qa = new QuestionAnswer();
        qa.setTranscript(transcript);
        qa.setQuestion("Q?");
        qa.setAnswer("A");
        qa.setCreateDateTime(LocalDateTime.now());

        when(questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId))
                .thenReturn(List.of(qa));

        List<TranscriptQAResponseDTO> result = questionAnswerService.getAllQAByTranscriptId(transcriptId);

        assertEquals(1, result.size());
        assertEquals("Q?", result.get(0).getQuestion());
    }

    @Test
    void getAllQAByTranscriptId_shouldThrowException_whenEmpty() {
        Long transcriptId = 1L;

        when(questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> {
            questionAnswerService.getAllQAByTranscriptId(transcriptId);
        });
    }

    @Test
    void createAndSaveQuestionAnswer_shouldReturnSavedEntity() {
        Long transcriptId = 1L;
        String question = "Test Q";
        String answer = "Test A";

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);

        QuestionAnswer savedQA = new QuestionAnswer();
        savedQA.setTranscript(transcript);
        savedQA.setQuestion(question);
        savedQA.setAnswer(answer);

        when(transcriptionService.getTranscriptById(transcriptId)).thenReturn(transcript);
        when(questionAnswersRepository.save(any())).thenReturn(savedQA);

        QuestionAnswer result = questionAnswerService.createAndSaveQuestionAnswer(transcriptId, question, answer);

        assertEquals(answer, result.getAnswer());
        verify(questionAnswersRepository, times(1)).save(any());
    }
}
