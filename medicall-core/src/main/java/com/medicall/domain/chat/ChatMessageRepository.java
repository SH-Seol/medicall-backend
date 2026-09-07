package com.medicall.domain.chat;

import java.time.LocalDateTime;
import java.util.List;

import com.medicall.domain.common.enums.SenderType;

import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository {
    void save(ChatMessage message);
    List<ChatMessage> findByChatRoomId(Long chatRoomId, Long cursorId, int size);
    List<ChatMessage> findAllByChatRoomId(Long chatRoomId);
    List<ChatMessage> findByChatRoomIdSince(Long chatRoomId, LocalDateTime since);
    void markAsRead(Long chatRoomId, SenderType readerType);
}
