package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.service.TranscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "1. Transcription", description = "Transcript APIs")
@Slf4j
@RestController
@RequestMapping("/api/v2/transcriptions")
public class TranscriptionController {
    @Autowired
    TranscriptionService generationService;

    @Operation(summary = "Generate a transcript based on the provided language",
    description = "This endpoint generates a transcript in the specific language, defaults to English if provided none.")

    @PostMapping("/transcripts")
    public ResponseEntity<TranscriptResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(generationService.generateTranscript(language));

    }

    @Operation(summary = "Get a generated transcript by its ID",
    description = "This endpoint retrieves a transcript by its ID. If the ID is not found in database ,it returns a 404 not found for that resource.")

    @GetMapping("/transcripts/{transcriptId}")
    public ResponseEntity<?> getTranscriptById(@PathVariable Long transcriptId) {
        return ResponseEntity.ok(generationService.getTranscriptDtoById(transcriptId));
    }

    @Operation(summary = "Get all the generated transcripts",
    description = "This endpoint retrieves all generated transcripts. It returns a list of all transcripts in the database.")

    @GetMapping("/transcripts")
    public ResponseEntity<?> getAllTranscripts() {
        return ResponseEntity.ok(generationService.getAllTranscripts());
    }




}
