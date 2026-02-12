package com.medicall.domain.chat.dto;

import java.util.List;

public record ChatCommand(
        String model,
        List<Message> messages,
        double temperature
) {
    public record Message(
            String role,
            String content
    ) {}

    public static ChatCommand of(String model, String role, String content, double temperature) {
        return new ChatCommand(model, List.of(new Message(role, content)), temperature);
    }
}
