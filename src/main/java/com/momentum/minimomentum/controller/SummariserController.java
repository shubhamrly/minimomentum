package com.momentum.minimomentum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "2. Summariser", description = "Summary APIs")
@RestController
@RequestMapping("/api/v2/summariser")
@RequiredArgsConstructor
public class SummariserController {

    private final SummaryService summaryService;

    @Operation(summary = "Generate a summary of the transcript by its transcript ID and specified language",
       description = "This endpoint generates a summary for the transcript by its transcriptID. If no language is provided, it defaults to English.")

    @PostMapping("/summaries")
    public ResponseEntity<SummaryResponseDTO> getSummary(@RequestParam Long transcriptId, @RequestParam(value = "language", defaultValue = "english") String language) throws JsonProcessingException {
        return ResponseEntity.ok(summaryService.generateSummary(transcriptId,language));
    }

    @Operation(summary = "Get a generated summary from db by its ID",
       description = "This endpoint retrieves a summary by its ID. If the ID is not found in the database, it returns a 404 not found for that resource.")

    @GetMapping("/summaries/{summaryId}")
    public ResponseEntity<?> getSummaryById(@PathVariable Long summaryId) {
        return ResponseEntity.ok(summaryService.getSummary(summaryId));

    }

    @Operation(summary = "Get all the generated summaries ",
       description = "This endpoint retrieves all generated summaries from database. It returns a list of all summaries persisting in the database.")
    @GetMapping("/summaries")
    public ResponseEntity<?> getAllSummaries() {
        return ResponseEntity.ok(summaryService.getAllSummaries());
    }
}
