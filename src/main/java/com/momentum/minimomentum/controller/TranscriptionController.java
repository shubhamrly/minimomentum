package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.TranscriptResponseDTO;
import com.momentum.minimomentum.dto.SummaryResponseDTO;
import com.momentum.minimomentum.service.GenerationService;
import com.momentum.minimomentum.service.SummaryService;
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
    @Autowired
    SummaryService summaryService;

    @PostMapping("/generateTranscript")
    public ResponseEntity<TranscriptResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(generationService.generateTranscript(language));

    }

    @GetMapping("/getTranscript/{transcriptId}")
    public ResponseEntity<?> getTranscript(@PathVariable String transcriptId) {
        return ResponseEntity.ok(generationService.getTranscript(transcriptId));
    }

    @GetMapping("/getAllTranscripts")
    public ResponseEntity<?> getAllTranscripts() {
        return ResponseEntity.ok(generationService.getAllTranscripts());
    }

    @PostMapping("/summariser/{transcriptId}")
    public ResponseEntity<SummaryResponseDTO> getSummary(@PathVariable String transcriptId,@RequestParam(value = "language", defaultValue = "english") String language) {
        return ResponseEntity.ok(summaryService.generateSummary(transcriptId,language));
    }
    
    @GetMapping("/getSummaryById/{summaryId}")
    public ResponseEntity<?> getSummaryById(@PathVariable String summaryId) {
        return ResponseEntity.ok(summaryService.getSummary(summaryId));

    }
    @GetMapping("/getAllSummaries")
    public ResponseEntity<?> getAllSummaries() {
        return ResponseEntity.ok(summaryService.getAllSummaries());
    }


}
