package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** AI Generated Code - Test skeleton structure **/

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
    void givenTranscriptId_whenGetAnswers_thenReturnGeneratedAnswerAndSaveIt() {
        Long transcriptId = 1L;
        String question = "What did customer say about sales product?";
        String transcriptText = PromptConstants.QUESTION_ANSWER_PROMPT_CONSTANT
                .replace("{transcriptId}", String.valueOf(transcriptId))
                .replace("{question}", question);
       
        String mockedAnswer = "The customer, Mr. Johnson, expressed interest in the Inventory Management System offered by RetailTech Solutions.";

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);
        transcript.setTranscriptText(transcriptText);

        QuestionAnswer savedQA = new QuestionAnswer();
        savedQA.setId(1L);
        savedQA.setTranscript(transcript);
        savedQA.setQuestion(question);
        savedQA.setAnswer(mockedAnswer);
        savedQA.setCreateDateTime(LocalDateTime.now());

        when(transcriptionService.getTranscriptById(transcriptId)).thenReturn(transcript);
        when(openAiClient.getCompletionOpenAi(anyString())).thenReturn(mockedAnswer);
        when(questionAnswersRepository.save(any(QuestionAnswer.class))).thenReturn(savedQA);

        String result = questionAnswerService.getAnswersByTranscriptId(transcriptId, question);

        assertEquals(mockedAnswer, result);
        verify(questionAnswersRepository, times(1)).save(any());
    }

    @Test
    void givenTranscriptIdWithAnswers_whenGetAllQA_thenReturnList() {

        Long transcriptId = 1L;

        Transcript transcript = new Transcript();
        transcript.setId(transcriptId);

        QuestionAnswer qa = new QuestionAnswer();
        qa.setTranscript(transcript);
        qa.setQuestion("What product customer was interested in?");
        qa.setAnswer("The customer was interested in the Inventory Management System by XYZ systems.");
        qa.setCreateDateTime(LocalDateTime.now());


        when(questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId))
                .thenReturn(List.of(qa));

        List<TranscriptQAResponseDTO> result = questionAnswerService.getAllQAByTranscriptId(transcriptId);

        assertEquals(1, result.size());
        assertEquals("What product customer was interested in?", result.get(0).getQuestion());
        assertEquals("The customer was interested in the Inventory Management System by XYZ systems.", result.get(0).getAnswer());
    }

    @Test
    void givenTranscriptIdWithNoAnswers_whenGetAllQA_thenThrowEntityNotFoundException() {
        Long transcriptId = 101L;

        when(questionAnswersRepository.findByTranscriptIdOrderByCreateDateTimeDesc(transcriptId))
                .thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> {
            questionAnswerService.getAllQAByTranscriptId(transcriptId);
        });
    }

    @Test
    void givenQuestionAndAnswer_whenCreateAndSave_thenReturnSavedEntity() {
        Long transcriptId = 1L;
        String question = "What product customer was interested in?";
        String answer = "The customer was interested in the Inventory Management System by XYZ systems.";

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
