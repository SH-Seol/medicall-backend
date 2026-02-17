package com.doctor.controller.v1.chat.dto;

import com.medicall.domain.chat.dto.ChatSummaryResult;

public record ChatSummaryResponse(
        String summary
) {
    public static ChatSummaryResponse from(ChatSummaryResult result){
        return new ChatSummaryResponse(result.summary());
    }
}
