package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.GenerationResponseDTO;
import com.momentum.minimomentum.service.GenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/v1/transcriptions")
public class TranscriptionController {
    @Autowired
    GenerationService generationService;

    @GetMapping("/generateTranscript")
    public ResponseEntity<GenerationResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(generationService.generateTranscript(language));

    }
}
