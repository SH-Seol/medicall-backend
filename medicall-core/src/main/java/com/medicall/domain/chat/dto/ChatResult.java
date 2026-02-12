package com.medicall.domain.chat.dto;

import java.util.List;

public record ChatResult(
        List<Choice> choices
) {
    public record Choice(
            int index,
            ChatCommand.Message message,
            String finishReason
    ){}
}
