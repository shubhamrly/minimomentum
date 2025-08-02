package com.momentum.minimomentum.service.openAiService;

import com.momentum.minimomentum.constant.PromptConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final  ChatClient chatClient;

    @Value("${spring.ai.openai.temperature}")
    private double temperature;


    public String getCompletionOpenAi(String userPrompt) {
        return chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(userPrompt)
                .call()
                .content();
    }

}
