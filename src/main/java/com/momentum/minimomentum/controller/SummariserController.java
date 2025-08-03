package com.momentum.minimomentum.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.momentum.minimomentum.dto.responseDTO.SummaryResponseDTO;
import com.momentum.minimomentum.service.SummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "2. Summariser", description = "Summary APIs")
@RestController
@RequestMapping("/api/v2/summariser")
@RequiredArgsConstructor

public class SummariserController {

    private final String className = getClass().getSimpleName();

    private final SummaryService summaryService;

    /**
     * Generates a summary for the transcript by its ID and specified language.
     * If no language is provided, it defaults to English.
     *
     * @param transcriptId the ID of the transcript to summarize
     * @param language the language for the summary (default is "English")
     * @return a ResponseEntity containing the generated summary
     * @throws JsonProcessingException if there is an error processing JSON
     */

    @Operation(summary = "Generate a summary of the transcript by its transcript ID and specified language",
            description = "This endpoint generates a summary for the transcript by its transcriptID. If no language is provided, it defaults to English.")

    @PostMapping("/summaries")
    public ResponseEntity<SummaryResponseDTO> getSummary(@RequestParam Long transcriptId, @RequestParam(value = "language", defaultValue = "english") String language) throws JsonProcessingException {

        log.info("[{}] Generating summary for transcript ID: {} in language: {}", className, transcriptId, language);

        return ResponseEntity.ok(summaryService.generateSummary(transcriptId, language));
    }

    /**
     * Retrieves a summary by its ID. If the ID is not found in the database, it
     * returns a 404 not found for that resource.
     *
     * @param summaryId the ID of the summary to retrieve
     * @return a ResponseEntity containing the summary if found
     */
    @Operation(summary = "Get a generated summary from db by its ID",
            description = "This endpoint retrieves a summary by its ID. If the ID is not found in the database, it returns a 404 not found for that resource.")

    @GetMapping("/summaries/{summaryId}")
    public ResponseEntity<?> getSummaryById(@PathVariable Long summaryId) {

        log.info("[{}] Fetching summary for transcript ID: {}", className, summaryId);

        return ResponseEntity.ok(summaryService.getSummary(summaryId));

    }

    /**
     * Retrieves all generated summaries from the database. It returns a list of
     * all summaries persisting in the database.
     *
     * @return a ResponseEntity containing a list of all summaries
     */
    @Operation(summary = "Get all the generated summaries ",
            description = "This endpoint retrieves all generated summaries from database. It returns a list of all summaries persisting in the database.")
    @GetMapping("/summaries")
    public ResponseEntity<?> getAllSummaries() {

        log.info("[{}] Fetching All summaries", className);

        return ResponseEntity.ok(summaryService.getAllSummaries());
    }
}
