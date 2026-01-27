package com.medicall.domain.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;

@Repository
public interface ChatRoomRepository {
    ChatRoom save(ChatRoom chatRoom);
    Optional<ChatRoom> findByAppointmentId(Long appointmentId, ChatRoomType type);
    List<ChatRoom> findByUserIdAndType(Long userId, SenderType type);
}
