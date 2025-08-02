package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.service.QuestionAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "3. Question Answer", description = "Question Answer APIs")
@Slf4j
@RestController
@RequestMapping("/api/v2/transcriptions")
@RequiredArgsConstructor
public class TranscriptQAController {

    private final String className = getClass().getSimpleName();

    private final QuestionAnswerService questionAnswerService;

    @Operation(
            summary = "Answers question based on generated transcript by transcriptId",
            description = "Generates answers to questions based on the provided transcript ID"
    )

    @PostMapping("/transcript/{transcriptId}/answer")
    public ResponseEntity<TranscriptQAResponseDTO> getAnswersByTranscriptId(@PathVariable Long transcriptId, @RequestBody String question) {
        String OpenAiAnswer = questionAnswerService.getAnswersByTranscriptId(transcriptId, question);
        TranscriptQAResponseDTO response = new TranscriptQAResponseDTO();
        response.setAnswer(OpenAiAnswer);

        log.info("[{}] getAnswersByTranscriptId called with transcriptId: {}, question: {}", className, transcriptId, question);

        return ResponseEntity.ok(response);

    }

    @Operation(
            summary = "Get all previously questions and their answers by transcriptId",
            description = "Fetches all previously asked questions and their answers ordered By Latest DateTime based on the provided transcript ID"
    )
    @GetMapping("/transcript/{transcriptId}/answers")
    public ResponseEntity<List<TranscriptQAResponseDTO>> getAllAnswersByTranscriptId(@PathVariable Long transcriptId) {

        log.info("[{}] Fetching all answers by transcriptId: {}", className, transcriptId);

        return ResponseEntity.ok(questionAnswerService.getAllQAByTranscriptId(transcriptId));
    }

}
