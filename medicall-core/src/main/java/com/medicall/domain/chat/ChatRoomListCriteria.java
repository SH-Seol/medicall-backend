package com.medicall.domain.chat;

import com.medicall.domain.common.enums.SenderType;

public record ChatRoomListCriteria(
        Long userId,
        SenderType senderType,
        Long cursorId,
        int size
) {
}
