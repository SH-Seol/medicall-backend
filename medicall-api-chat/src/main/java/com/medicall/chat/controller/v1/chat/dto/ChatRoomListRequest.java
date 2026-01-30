package com.medicall.chat.controller.v1.chat.dto;

import io.swagger.v3.oas.annotations.Parameter;

import com.medicall.domain.chat.ChatRoomListCriteria;
import com.medicall.domain.common.enums.SenderType;

public record ChatRoomListRequest(
        @Parameter(description = "커서 ID (이전 페이지의 마지막 ID, null이면 최신순)", example = "100")
        Long cursorId,
        @Parameter(description = "조회 개수", example = "20")
        int size
) {
    public ChatRoomListCriteria toCriteria(Long userId, SenderType type){
        return new ChatRoomListCriteria(userId, type, cursorId, size);
    }
}
