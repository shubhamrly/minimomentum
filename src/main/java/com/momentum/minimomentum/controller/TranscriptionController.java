package com.momentum.minimomentum.controller;

import com.momentum.minimomentum.dto.responseDTO.TranscriptResponseDTO;
import com.momentum.minimomentum.service.TranscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1. Transcription", description = "Transcript APIs")
@Slf4j
@RestController
@RequestMapping("/api/v2/transcriptions")
@RequiredArgsConstructor
public class TranscriptionController {

    private final String className = getClass().getSimpleName();

    private final TranscriptionService generationService;

    /**
     * Generates a transcript based on the provided language. If no language is
     * provided, it defaults to English.
     *
     * @param language the language for the transcript (default is "English")
     * @return a ResponseEntity containing the generated transcript
     */
    @Operation(summary = "Generate a transcript based on the provided language",
            description = "This endpoint generates a transcript in the specific language," +
                    " defaults to English if provided none.")

    @PostMapping("/transcripts")
    public ResponseEntity<TranscriptResponseDTO> generateTranscript(@RequestParam(value = "language", defaultValue = "english") String language) {

        log.info("[{}] Generating transcript in language: {}", className, language);

        return ResponseEntity.ok(generationService.generateTranscript(language));

    }

    /**
     * Retrieves a transcript by its ID. If the ID is not found in the database,
     * it returns a 404 not found for that resource.
     *
     * @param transcriptId the ID of the transcript to retrieve
     * @return a ResponseEntity containing the transcript if found
     */
    @Operation(summary = "Get a generated transcript by its ID",
            description = "This endpoint retrieves a transcript by its ID. " +
                    "If the ID is not found in database ," +
                    "it returns a 404 not found for that resource.")

    @GetMapping("/transcripts/{transcriptId}")
    public ResponseEntity<TranscriptResponseDTO> getTranscriptById(@PathVariable Long transcriptId) {
        log.info("[{}] Fetching transcript for ID: {}", className, transcriptId);
        return ResponseEntity.ok(generationService.getTranscriptDtoById(transcriptId));
    }

    /**
     * Retrieves all generated transcripts from the database.
     *
     * @return a ResponseEntity containing a list of all transcripts
     */
    @Operation(summary = "Get all the generated transcripts",
            description = "This endpoint retrieves all generated transcripts. " +
                    "It returns a list of all transcripts in the database.")

    @GetMapping("/transcripts")
    public ResponseEntity<List<TranscriptResponseDTO>> getAllTranscripts() {

        log.info("[{}] Fetching all transcripts", className);

        return ResponseEntity.ok(generationService.getAllTranscripts());
    }

}
