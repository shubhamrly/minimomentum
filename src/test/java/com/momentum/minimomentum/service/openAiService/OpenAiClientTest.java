package com.momentum.minimomentum.service.openAiService;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.exception.OpenAiClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAiClientTest {

    private ChatClient chatClient;
    private OpenAiClient openAiClient;

    @BeforeEach
    void setUp() {
        chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        openAiClient = new OpenAiClient(chatClient);
    }

    @Test
    void testGetCompletionOpenAi_Success() {
        // Given
        String prompt = "What is Java?";
        String expectedResponse = "Java is a programming language.";

        when(chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(prompt)
                .call()
                .content()).thenReturn(expectedResponse);

        // When
        String result = openAiClient.getCompletionOpenAi(prompt);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void testGetCompletionOpenAi_ExceptionThrown() {
        // Given
        String prompt = "Something wrong";

        when(chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(prompt)
                .call()
                .content()).thenThrow(new RuntimeException("Service unavailable"));

        // Then
        assertThrows(OpenAiClientException.class, () -> {
            openAiClient.getCompletionOpenAi(prompt);
        });
    }
}
