package com.medicall.domain.chat;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository {
    void save(ChatMessage message);
    List<ChatMessage> findByChatRoomId(Long chatRoomId, Long cursorId, int size);
}
