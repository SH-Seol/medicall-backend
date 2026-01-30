package com.medicall.chat.controller.v1.chat.dto;

import java.time.LocalDateTime;

import com.medicall.domain.chat.ChatRoom;

public record ChatRoomResponse(
        Long id,
        Long patientId,
        Long doctorId,
        Long hospitalId,
        LocalDateTime lastMessageTime
) {
    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(chatRoom.id(), chatRoom.patientId(), chatRoom.doctorId(), chatRoom.hospitalId(), chatRoom.lastMessageTime());
    }
}
