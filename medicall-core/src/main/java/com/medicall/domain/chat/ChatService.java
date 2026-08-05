package com.medicall.domain.chat;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.chat.dto.ChatMessageListCriteria;
import com.medicall.domain.chat.dto.ChatRoomListCriteria;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.domain.chat.dto.ChatRoomSummary;
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

    @Transactional
    public CursorPageResult<ChatMessage> getChatMessages(ChatMessageListCriteria criteria, Long userId, SenderType senderType) {
        chatValidator.validateChatRoomAccess(criteria.chatRoomId(), userId, senderType);

        CursorPageResult<ChatMessage> messages = chatMessageReader.read(criteria.chatRoomId(), criteria.cursorId(), criteria.size());

        // 방을 열었으므로 상대가 보낸 메시지는 읽은 것으로 처리한다.
        chatMessageWriter.markAsRead(criteria.chatRoomId(), senderType);

        return messages;
    }

    @Transactional(readOnly = true)
    public CursorPageResult<ChatRoomSummary> getChatRooms(ChatRoomListCriteria criteria) {
        List<ChatRoomSummary> summaries = chatRoomReader.getChatRoomSummaries(criteria.userId(), criteria.senderType());

        return CursorPageResult.of(summaries, criteria.cursorId());
    }
}
