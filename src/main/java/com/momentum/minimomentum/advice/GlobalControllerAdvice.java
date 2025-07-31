package com.momentum.minimomentum.advice;

import com.momentum.minimomentum.exception.PromptNotFoundException;
import com.momentum.minimomentum.exception.SummaryNotFoundException;
import com.momentum.minimomentum.exception.TranscriptNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(PromptNotFoundException.class)
    public ResponseEntity<String> handlePromptNotFoundException(Exception e) {
        return ResponseEntity
                .badRequest()
                .body("Prompt not found: " + e.getMessage());
    }
    @ExceptionHandler(SummaryNotFoundException.class)
    public ResponseEntity<String> handleSummaryNotFoundException(Exception e) {
        return ResponseEntity
                .badRequest()
                .body("Summary not found: " + e.getMessage());
    }

    @ExceptionHandler(TranscriptNotFoundException.class)
    public ResponseEntity<String> handleTranscriptNotFoundException(Exception e) {
        return ResponseEntity
                .badRequest()
                .body("Transcript not found: " + e.getMessage());
    }
}
