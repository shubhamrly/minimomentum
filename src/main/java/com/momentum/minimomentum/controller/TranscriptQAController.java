package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptQAResponseDTO;
import com.momentum.minimomentum.dto.requestDTO.TranscriptQARequestDTO;
import com.momentum.minimomentum.service.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptQAController {

    @Autowired
    QuestionAnswerService questionAnswerService;

    @PostMapping("answers/{transcriptId}")
    public ResponseEntity<TranscriptQAResponseDTO> getAnswersByTranscriptId(@RequestBody TranscriptQARequestDTO request) {
        String transcriptId = request.getTranscriptID();
        String question = request.getQuestion();
        String  OpenAiAnswer =  questionAnswerService.getAnswersByTranscriptId(transcriptId, question);

        TranscriptQAResponseDTO response = new TranscriptQAResponseDTO();
        response.setAnswer(OpenAiAnswer);
        return ResponseEntity.ok(response);

    }
    @GetMapping("getAllAnswersById/{transcriptId}")
    public ResponseEntity<?> getAllAnswersByTranscriptId(@PathVariable String transcriptId) {
        return ResponseEntity.ok(questionAnswerService.getAllQAByTranscriptId(transcriptId));
    }

}
