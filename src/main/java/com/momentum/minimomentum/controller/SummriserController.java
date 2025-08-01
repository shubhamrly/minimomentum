package com.momentum.minimomentum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/summariser")
public class SummriserController {
    @Autowired
    SummaryService summaryService;

    @PostMapping("/summariserById/{transcriptId}")
    public ResponseEntity<SummaryResponseDTO> getSummary(@PathVariable String transcriptId, @RequestParam(value = "language", defaultValue = "english") String language) throws JsonProcessingException {
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
