package com.momentum.minimomentum.service;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.dto.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient openAiWebClient;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.temperature}")
    private double temperature;

    @Value("${openai.api.chat-completions-uri}")
    private String chatCompletionsUri;

    public String getCompletion(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty.");
        }

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "temperature", temperature,
                "messages", List.of(
                        Map.of("role", "system", "content", PromptConstants.SYSTEM_CONTEXT_CONSTANT),
                        Map.of("role", "user", "content", prompt)
                )
        );

        long startTime = System.currentTimeMillis();

        try {
            ChatResponseDTO chatResponse = openAiWebClient.post()
                    .uri(chatCompletionsUri)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ChatResponseDTO.class)
                    .block();

            String responseText = chatResponse != null && chatResponse.getFirstMessageContent() != null
                    ? chatResponse.getFirstMessageContent().trim()
                    : "";

            long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
            log.info("OpenAI API call time taken: {}s", timeTaken);
            log.info("OpenAI response length: {}", responseText.length());
            // Normalize whitespace
            return responseText.replaceAll("\\n", "  ");

        } catch (WebClientResponseException ex) {
            log.error("OpenAI API error ({}): {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to get response from OpenAI", ex);
        } catch (Exception ex) {
            log.error("Unexpected error during OpenAI call", ex);
            throw new RuntimeException("Unexpected error from OpenAI client", ex);
        }
    }
}
