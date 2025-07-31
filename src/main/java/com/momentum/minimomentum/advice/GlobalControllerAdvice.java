package com.momentum.minimomentum.advice;

import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.exception.PromptNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({PromptNotFoundException.class})
    public ResponseEntity<String> handlePromptNotFoundException(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body("Prompt not found: " + e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity
                .internalServerError()
                .body("An unexpected error occurred: " + e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity
                .notFound()
                .build();
    }
}
