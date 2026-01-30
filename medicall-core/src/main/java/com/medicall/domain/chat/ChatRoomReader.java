package com.medicall.domain.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomReader(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Optional<ChatRoom> getChatRoomByAppointmentIdAndType(Long appointmentId, ChatRoomType type) {
        return chatRoomRepository.findByAppointmentId(appointmentId, type);
    }

    public List<ChatRoom> getChatRoomList(Long userId, SenderType senderType) {
        return chatRoomRepository.findByUserIdAndType(userId, senderType);
    }

    public ChatRoom getChatRoomById(Long chatRoomId) {
        Optional<ChatRoom> opChatRoom =  chatRoomRepository.findById(chatRoomId);
        if (opChatRoom.isPresent()) {
            return opChatRoom.get();
        }
        throw new CoreException(CoreErrorType.CHATROOM_NOT_FOUND);
    }
}
