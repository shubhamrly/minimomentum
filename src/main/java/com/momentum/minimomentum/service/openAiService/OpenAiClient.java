package com.momentum.minimomentum.service.openAiService;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.exception.OpenAiClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

/*
 * OpenAiClient is a service that interacts with the OpenAI API to get completions based on user prompts.
 * It uses the ChatClient to send prompts and receive responses.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final ChatClient chatClient;

    @Value("${spring.ai.openai.temperature}")
    private double temperature;

    public String getCompletionOpenAi(String userPrompt) {
        LocalDateTime startTime = LocalDateTime.now();

        try{
        String content = chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(userPrompt)
                .call()
                .content();
        LocalDateTime endTime = LocalDateTime.now();
        log.debug("[{}] Time taken by open-ai: {}",getClass().getSimpleName(), Duration.between(endTime, startTime));
        return content;
    }catch (Exception e) {
            log.error("[{}] Error while getting completion from OpenAI: {}", getClass().getSimpleName(), e.getMessage());
            throw new OpenAiClientException("Error while getting completion from OpenAI");
        }
    }

}
