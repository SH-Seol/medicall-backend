package com.medicall.domain.chat;

public record ChatMessageListCriteria(
        Long chatRoomId,
        Long cursorId,
        int size
) {}
