package com.medicall.domain.chat;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ChatValidator chatValidator;

    public ChatService(
            ChatMessageWriter chatMessageWriter,
            ChatMessageReader chatMessageReader,
            ChatRoomReader chatRoomReader,
            ChatRoomWriter chatRoomWriter,
            ChatValidator chatValidator
    ) {
        this.chatMessageWriter = chatMessageWriter;
        this.chatMessageReader = chatMessageReader;
        this.chatRoomReader = chatRoomReader;
        this.chatRoomWriter = chatRoomWriter;
        this.chatValidator = chatValidator;
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

    @Transactional(readOnly = true)
    public CursorPageResult<ChatMessage> getChatMessages(ChatMessageListCriteria criteria, Long userId, SenderType senderType) {
        chatValidator.validateChatRoomAccess(criteria.chatRoomId(), userId, senderType);
        return chatMessageReader.read(criteria.chatRoomId(), criteria.cursorId(), criteria.size());
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getChatRooms(Long userId, SenderType senderType) {
        return chatRoomReader.getChatRoomList(userId, senderType);
    }
}
