package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import com.momentum.minimomentum.service.GenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptionController {
    @Autowired
    GenerationService generationService;


    @PostMapping("/generateTranscript")
    public ResponseEntity<TranscriptResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(generationService.generateTranscript(language));

    }

    @GetMapping("/getTranscriptById/{transcriptId}")
    public ResponseEntity<?> getTranscript(@PathVariable String transcriptId) {
        return ResponseEntity.ok(generationService.getTranscript(transcriptId));
    }

    @GetMapping("/getAllTranscripts")
    public ResponseEntity<?> getAllTranscripts() {
        return ResponseEntity.ok(generationService.getAllTranscripts());
    }




}
