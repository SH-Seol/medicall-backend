package com.medicall.domain.chat.dto;

import java.time.LocalDateTime;

import com.medicall.domain.common.enums.ChatRoomType;

/**
 * 채팅방 목록 화면에 필요한 정보를 한 번에 담는다.
 * 상대방 이름을 따로 조회하지 않도록 함께 내려준다.
 */
public record ChatRoomSummary(
        Long chatRoomId,
        Long appointmentId,
        ChatRoomType chatRoomType,
        Long counterpartId,
        String counterpartName,
        String counterpartImageUrl,
        String lastMessage,
        LocalDateTime lastMessageTime,
        long unreadCount
) {
}
