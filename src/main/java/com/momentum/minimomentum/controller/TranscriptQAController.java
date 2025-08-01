package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.service.QuestionAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptQAController {

    @Autowired
    QuestionAnswerService questionAnswerService;
    @Operation(
            summary = "Answers question based on generated transcript by transcriptId",
            description = "Generates answers to questions based on the provided transcript ID"
    )

    @PostMapping("answers/{transcriptId}")
    public ResponseEntity<TranscriptQAResponseDTO> getAnswersByTranscriptId(@PathVariable Long transcriptId, @RequestBody String question) {
        String  OpenAiAnswer =  questionAnswerService.getAnswersByTranscriptId(transcriptId, question);
        TranscriptQAResponseDTO response = new TranscriptQAResponseDTO();
        response.setAnswer(OpenAiAnswer);
        return ResponseEntity.ok(response);

    }
    @Operation(
            summary = "Get all previously questions and their answers by transcriptId",
            description = "Fetches all previously asked questions and their answers based on the provided transcript ID"
    )
    @GetMapping("getAllAnswersById/{transcriptId}")
    public ResponseEntity<List<TranscriptQAResponseDTO>> getAllAnswersByTranscriptId(@PathVariable Long transcriptId) {
        log.info("TranscriptQAController || getAllAnswersByTranscriptId called with transcriptId: {}", transcriptId);
        return ResponseEntity.ok(questionAnswerService.getAllQAByTranscriptId(transcriptId));
    }

}
