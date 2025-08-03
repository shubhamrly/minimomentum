package com.momentum.minimomentum.advice;

import com.momentum.minimomentum.exception.EntityNotFoundException;
import com.momentum.minimomentum.exception.OpenAiClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    // General 500 errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Internal Server error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .internalServerError()
                .body("Internal server error occurred: " + e.getMessage());
    }

    // when an entity is not found in the database.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
      log.error("Entity Not found: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    
    // when there is an error with the OpenAI client.
    @ExceptionHandler(OpenAiClientException.class)
    public ResponseEntity<String> handleOpenAiClientException(OpenAiClientException e) {
        log.error("OpenAI Client error: {}", e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body("OpenAI Client error: " + e.getMessage());
    }

}
