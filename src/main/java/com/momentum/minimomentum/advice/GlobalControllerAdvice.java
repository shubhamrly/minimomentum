package com.momentum.minimomentum.advice;

import com.momentum.minimomentum.exception.PromptNotFoundException;
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
}
