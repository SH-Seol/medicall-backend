package com.medicall.domain.chat;

import java.time.LocalDateTime;

import com.medicall.domain.common.enums.SenderType;

public record ChatMessage(
        Long id,
        Long chatRoomId,
        SenderType senderType,
        Long senderId,
        String content,
        boolean read,
        LocalDateTime createdAt
) {
}
