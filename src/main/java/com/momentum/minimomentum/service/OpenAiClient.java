package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private double temperature;
    @Value("${openai.api.base-url}")
    private String baseUrl;
    @Value("${openai.api.chat-completions-uri}")
    private String chatCompletionsUri;

    private WebClient webClient;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getCompletion(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", temperature,
                "messages", List.of(
                        Map.of("role", "system", "content", PromptConstants.SYSTEM_CONTEXT_CONSTANT),
                        Map.of("role", "user", "content", prompt)
                )
        );

        String responseOpenAi =  webClient.post()
                .uri(chatCompletionsUri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    Object choicesObj = response.get("choices");
                    if (choicesObj instanceof List<?> choices && !choices.isEmpty()) {
                        Object first = choices.get(0);
                        if (first instanceof Map<?,?> firstMap) {
                            Object messageObj = ((Map<?,?>) firstMap).get("message");
                            if (messageObj instanceof Map<?,?> messageMap) {
                                Object content = ((Map<?,?>) messageMap).get("content");
                                return content != null ? content.toString() : "";
                            }
                        }
                    }
                    return "";
                })
                .block();
        assert responseOpenAi != null;
        log.info("OpenAI response length: {}", responseOpenAi.length());
        return responseOpenAi;
    }
}