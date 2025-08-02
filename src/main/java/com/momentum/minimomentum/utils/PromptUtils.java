package com.momentum.minimomentum.utils;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.constant.PromptType;
import com.momentum.minimomentum.exception.PromptNotFoundException;

public class PromptUtils {

    public static String getPrompt(PromptType type, String language) {
        return switch (type) {
            case GENERATION_PROMPT ->
                String.format(PromptConstants.GENERATION_PROMPT_CONSTANT, language);
            case SUMMARY_PROMPT ->
                String.format(PromptConstants.SUMMARY_PROMPT_CONSTANT, language);
            case QUESTION_ANSWERING_PROMPT ->
                PromptConstants.QUESTION_ANSWERING_PROMPT_CONSTANT;
            default ->
                throw new PromptNotFoundException("Unsupported prompt type: " + type);
        };
    }
}
