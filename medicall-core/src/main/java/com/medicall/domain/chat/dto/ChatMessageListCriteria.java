package com.medicall.domain.chat.dto;

public record ChatMessageListCriteria(
        Long chatRoomId,
        Long cursorId,
        int size
) {}
