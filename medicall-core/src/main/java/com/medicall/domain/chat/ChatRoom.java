package com.medicall.domain.chat;

import java.time.LocalDateTime;

import com.medicall.domain.common.enums.ChatRoomType;

public record ChatRoom(
        Long id,
        Long appointmentId,
        Long patientId,
        Long doctorId,     // nullable
        Long hospitalId,   // nullable
        ChatRoomType type,
        LocalDateTime lastMessageTime
) {

    public static ChatRoom forDoctor(
            Long appointmentId,
            Long patientId,
            Long doctorId
    ) {
        return new ChatRoom(
                null,
                appointmentId,
                patientId,
                doctorId,
                null,
                ChatRoomType.PATIENT_DOCTOR,
                null
        );
    }

    public static ChatRoom forHospital(
            Long appointmentId,
            Long patientId,
            Long hospitalId
    ) {
        return new ChatRoom(
                null,
                appointmentId,
                patientId,
                null,
                hospitalId,
                ChatRoomType.PATIENT_HOSPITAL,
                null
        );
    }
}
