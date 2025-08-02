package com.momentum.minimomentum.advice;

import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.exception.PromptNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler({PromptNotFoundException.class})
    public ResponseEntity<String> handlePromptNotFoundException(Exception e) {
        log.error("Prompt Not found: {}", e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body("Prompt not found: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Internal Server error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .internalServerError()
                .body("Internal server error occurred: " + e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
      log.error("Entity Not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
