package com.momentum.minimomentum.service.openAiService;

import com.momentum.minimomentum.constant.PromptConstants;
import com.momentum.minimomentum.exception.OpenAiClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * AI Generated Code - Test skeleton structure *
 */
class OpenAiClientTest {

    private ChatClient chatClient;
    private OpenAiClient openAiClient;

    @BeforeEach
    void setUp() {
        chatClient = mock(ChatClient.class, RETURNS_DEEP_STUBS);
        openAiClient = new OpenAiClient(chatClient);
    }

    @Test
    void given_validPrompt_when_getCompletion_then_returns200() {

        String prompt = PromptConstants.QUESTION_ANSWER_PROMPT_CONSTANT
                .replace("{transcriptId}", "1")
                .replace("{question}", "What did customer say about sales product?");

        String expectedResponse = "The customer, Mr. Johnson, was interested in the Inventory Management System offered by RetailTech Solutions.";

        when(chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(prompt)
                .call()
                .content()).thenReturn(expectedResponse);

        String result = openAiClient.getCompletionOpenAi(prompt);

        assertEquals(expectedResponse, result);
    }

    @Test
    void given_openAiDown_when_getCompletion_then_returns503() {
        String prompt = PromptConstants.QUESTION_ANSWER_PROMPT_CONSTANT
                .replace("{transcriptId}", "1")
                .replace("{question}", "What did customer say about sales product?");

        when(chatClient.prompt()
                .system(PromptConstants.SYSTEM_CONTEXT_CONSTANT)
                .user(prompt)
                .call()
                .content()).thenThrow(new OpenAiClientException("Service unavailable"));

        assertThrows(OpenAiClientException.class, () -> {
            openAiClient.getCompletionOpenAi(prompt);
        });
    }
}
