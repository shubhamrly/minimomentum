package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.TranscriptQAResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptQAController {
    @GetMapping("answers/{transcriptId}")
    public ResponseEntity<List<TranscriptQAResponseDTO>> getAllQAsByTranscript(
            @PathVariable String transcriptId) {
        Object transcriptQaService;
        return ResponseEntity.ok(
               .getAllQaByTranscriptId(transcriptId));
    }

}
