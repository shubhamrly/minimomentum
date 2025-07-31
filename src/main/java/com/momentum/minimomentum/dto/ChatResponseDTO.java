package com.momentum.minimomentum.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatResponseDTO {

    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }


    public String getFirstMessageContent() {
        if (choices != null && !choices.isEmpty()) {
            Message message = choices.get(0).getMessage();
            if (message != null) return message.getContent();
        }
        return null;
    }
}


