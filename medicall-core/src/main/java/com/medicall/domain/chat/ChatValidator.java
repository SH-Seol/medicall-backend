package com.medicall.domain.chat;

import java.util.Objects;

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

        // doctorId/hospitalId는 채팅방 종류에 따라 null이므로 null-safe하게 비교한다.
        Long participantId = switch (type) {
            case DOCTOR -> chatRoom.doctorId();
            case PATIENT -> chatRoom.patientId();
            case HOSPITAL -> chatRoom.hospitalId();
        };

        if(!Objects.equals(participantId, userId)){
            throw new CoreException(CoreErrorType.CHATROOM_MOT_ACCESSIBLE);
        }
    }
}
