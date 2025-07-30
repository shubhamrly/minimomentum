package com.momentum.minimomentum.prompt.impl;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.exception.PromptNotFoundException;
import org.springframework.stereotype.Component;



@Component
public class PromptFactory {

    public String getPrompt(PromptType type, String language) throws PromptNotFoundException {
        return switch (type) {
            case GENERATION_PROMPT -> generationPrompt(language);
            default -> throw new PromptNotFoundException("Unsupported prompt type: " + type);
        };
    }

    private String generationPrompt(String language) {
        return String.format(PromptConstants.GENERATION_PROMPT_CONSTANT, language);
    }

}
