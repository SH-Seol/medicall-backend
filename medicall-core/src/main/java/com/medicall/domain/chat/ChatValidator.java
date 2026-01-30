package com.medicall.domain.chat;

import org.springframework.stereotype.Component;

import com.medicall.domain.common.enums.SenderType;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class ChatValidator {

    private final ChatRoomReader chatRoomReader;

    public ChatValidator(ChatRoomReader chatRoomReader) {
        this.chatRoomReader = chatRoomReader;
    }

    public void validateChatRoomAccess(Long chatRoomId, Long userId, SenderType type) {
        ChatRoom chatRoom = chatRoomReader.getChatRoomById(chatRoomId);

        switch (type) {
            case DOCTOR -> {
                if(!chatRoom.doctorId().equals(userId)){
                    throw new CoreException(CoreErrorType.CHATROOM_MOT_ACCESSIBLE);
                }
            }
            case PATIENT -> {
                if(!chatRoom.patientId().equals(userId)){
                    throw new CoreException(CoreErrorType.CHATROOM_MOT_ACCESSIBLE);
                }
            }
            case HOSPITAL -> {
                if(!chatRoom.hospitalId().equals(userId)){
                throw new CoreException(CoreErrorType.CHATROOM_MOT_ACCESSIBLE);
                }
            }
        }
    }
}
