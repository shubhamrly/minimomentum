package com.momentum.minimomentum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TranscriptController {
    @GetMapping("/generateTranscript")
    public String generateTranscript() {
        // Logic to generate a transcript
        // This is a placeholder for the actual implementation
        return "Transcript generated successfully!";
    }
}
