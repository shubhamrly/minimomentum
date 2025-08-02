package com.momentum.minimomentum.service.openAiService;

import com.momentum.minimomentum.constant.PromptConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final ChatClient chatClient;

    @Value("${spring.ai.openai.temperature}")
    private double temperature;

    public String getCompletionOpenAi(String userPrompt) {
        LocalDateTime startTime = LocalDateTime.now();

        log.info("Prompt: {}",userPrompt);

        String content = chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(userPrompt)
                .call()
                .content();
        LocalDateTime endTime = LocalDateTime.now();
        log.debug("[{}] Time taken by open-ai: {}",getClass().getSimpleName(), Duration.between(endTime, startTime));
        return content;
    }

}
