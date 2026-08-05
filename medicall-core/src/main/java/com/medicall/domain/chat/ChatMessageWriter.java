package com.medicall.domain.chat;

import org.springframework.stereotype.Component;

import com.medicall.domain.common.enums.SenderType;

@Component
public class ChatMessageWriter {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageWriter(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void write(ChatMessage message) {
        chatMessageRepository.save(message);
    }

    public void markAsRead(Long chatRoomId, SenderType readerType) {
        chatMessageRepository.markAsRead(chatRoomId, readerType);
    }
}
