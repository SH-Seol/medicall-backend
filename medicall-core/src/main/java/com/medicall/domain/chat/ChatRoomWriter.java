package com.medicall.domain.chat;

import org.springframework.stereotype.Component;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.ChatRoomType;

@Component
public class ChatRoomWriter {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomWriter(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom create(Appointment appointment, ChatRoomType chatRoomType) {
        switch (chatRoomType) {
            case PATIENT_DOCTOR :
                return chatRoomRepository.save(
                        new ChatRoom(
                                null,
                                appointment.id(),
                                appointment.patient().id(),
                                appointment.doctor().id(),
                                null,
                                chatRoomType,
                                null
                        )
                );
            case PATIENT_HOSPITAL:
                return chatRoomRepository.save(
                        new ChatRoom(
                                null,
                                appointment.id(),
                                appointment.patient().id(),
                                null,
                                appointment.hospital().id(),
                                chatRoomType,
                                null
                        )
                );
            default: throw new IllegalArgumentException("채팅 생성 불가, 잘못된 접근");
        }
    }
}
