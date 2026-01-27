package com.medicall.domain.chat;

import org.springframework.stereotype.Component;

@Component
public class ChatMessageWriter {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageWriter(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void write(ChatMessage message) {
        chatMessageRepository.save(message);
    }
}
