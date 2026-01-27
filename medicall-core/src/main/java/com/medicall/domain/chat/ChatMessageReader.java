package com.medicall.domain.chat;

import java.util.List;

import org.springframework.stereotype.Component;

import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;

@Component
public class ChatMessageReader {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageReader(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public CursorPageResult<ChatMessage> read(Long chatRoomId, Long cursorId, int size) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId, cursorId, size);

        return CorePageUtils.buildCursorResult(messages, size, ChatMessage::id);
    }
}
