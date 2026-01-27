package com.medicall.domain.chat;

import java.awt.Cursor;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.support.CursorPageResult;

@Service
public class ChatService {

    private final ChatMessageWriter chatMessageWriter;
    private final ChatMessageReader chatMessageReader;
    private final ChatRoomReader chatRoomReader;
    private final ChatRoomWriter chatRoomWriter;

    public ChatService(
            ChatMessageWriter chatMessageWriter,
            ChatMessageReader chatMessageReader,
            ChatRoomReader chatRoomReader,
            ChatRoomWriter chatRoomWriter
    ) {
        this.chatMessageWriter = chatMessageWriter;
        this.chatMessageReader = chatMessageReader;
        this.chatRoomReader = chatRoomReader;
        this.chatRoomWriter = chatRoomWriter;
    }

    @Transactional
    public void sendMessage(
            Long chatRoomId,
            SenderType senderType,
            Long userId,
            String content
    ) {
        ChatMessage message = new ChatMessage(
                null,
                chatRoomId,
                senderType,
                userId,
                content,
                false,
                null
        );

        chatMessageWriter.write(message);
    }

    @Transactional
    public ChatRoom getOrCreate(Appointment appointment, ChatRoomType chatRoomType){
        return chatRoomReader.getChatRoomByAppointmentIdAndType(appointment.id(), chatRoomType)
                .orElseGet(() -> chatRoomWriter.create(appointment, chatRoomType));
    }

    public CursorPageResult<ChatMessage> getChatMessages(Long chatRoomId, Long cursorId, int size) {
        return chatMessageReader.read(chatRoomId, cursorId, size);
    }

    public List<ChatRoom> getChatRooms(Long userId, SenderType senderType) {
        return chatRoomReader.getChatRoomList(userId, senderType);
    }
}
