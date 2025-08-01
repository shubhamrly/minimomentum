package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.service.TranscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptionController {
    @Autowired
    TranscriptionService generationService;


    @PostMapping("/generateTranscript")
    public ResponseEntity<TranscriptResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(generationService.generateTranscript(language));

    }

    @GetMapping("/getTranscriptById/{transcriptId}")
    public ResponseEntity<?> getTranscriptById(@PathVariable Long transcriptId) {
        return ResponseEntity.ok(generationService.getTranscriptById(transcriptId));
    }

    @GetMapping("/getAllTranscripts")
    public ResponseEntity<?> getAllTranscripts() {
        return ResponseEntity.ok(generationService.getAllTranscripts());
    }




}
