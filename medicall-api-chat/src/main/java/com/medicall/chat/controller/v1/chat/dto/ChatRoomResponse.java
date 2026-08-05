package com.medicall.chat.controller.v1.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.chat.dto.ChatRoomSummary;

@Schema(description = "채팅방 목록 응답")
public record ChatRoomResponse(
        @Schema(description = "채팅방 id")
        Long chatRoomId,

        @Schema(description = "연결된 예약 id")
        Long appointmentId,

        @Schema(description = "채팅방 종류", example = "PATIENT_DOCTOR")
        String chatRoomType,

        @Schema(description = "상대방 id (환자에게는 의사·병원, 의사·병원에게는 환자)")
        Long counterpartId,

        @Schema(description = "상대방 이름", example = "김의사")
        String counterpartName,

        @Schema(description = "상대방 프로필 이미지", nullable = true)
        String counterpartImageUrl,

        @Schema(description = "마지막 메시지 내용 (없으면 null)", nullable = true)
        String lastMessage,

        @Schema(description = "마지막 메시지 시각", nullable = true)
        LocalDateTime lastMessageTime,

        @Schema(description = "읽지 않은 메시지 수", example = "3")
        long unreadCount
) {
    public static ChatRoomResponse from(ChatRoomSummary summary) {
        return new ChatRoomResponse(
                summary.chatRoomId(),
                summary.appointmentId(),
                summary.chatRoomType().name(),
                summary.counterpartId(),
                summary.counterpartName(),
                summary.counterpartImageUrl(),
                summary.lastMessage(),
                summary.lastMessageTime(),
                summary.unreadCount()
        );
    }
}
