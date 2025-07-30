package com.momentum.minimomentum.prompt.impl;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.exception.PromptNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PromptFactory {

    public String getPrompt(PromptType type, String language) throws PromptNotFoundException {
        return switch (type) {

            case GENERATION_PROMPT -> generationPrompt(language);

            case SUMMARY_PROMPT ->  summaryPrompt(language);

            default -> throw new PromptNotFoundException("Unsupported prompt type: " + type);
        };
    }

    private String generationPrompt(String language) {
        return String.format(PromptConstants.GENERATION_PROMPT_CONSTANT, language);
    }
    private String summaryPrompt(String language) {

        log.info("Generating summary prompt for language: {}", language);

        return String.format(PromptConstants.SUMMARY_PROMPT_CONSTANT, language);
    }


}
