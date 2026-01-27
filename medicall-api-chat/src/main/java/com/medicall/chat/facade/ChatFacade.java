package com.medicall.chat.facade;

import org.springframework.stereotype.Component;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentReader;
import com.medicall.domain.appointment.AppointmentValidator;
import com.medicall.domain.chat.ChatRoom;
import com.medicall.domain.chat.ChatService;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;

@Component
public class ChatFacade {

    private final ChatService chatService;
    private final AppointmentReader appointmentReader;
    private final AppointmentValidator appointmentValidator;

    public ChatFacade(ChatService chatService, AppointmentReader appointmentReader,
                      AppointmentValidator appointmentValidator) {
        this.chatService = chatService;
        this.appointmentReader = appointmentReader;
        this.appointmentValidator = appointmentValidator;
    }

    public void sendMessage(CurrentUser user, Long appointmentId, String content) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        SenderType senderType = toSenderType(user);
        ChatRoomType chatRoomType = resolveChatRoomType(user.serviceType());

        appointmentValidator.validateParticipant(appointment, user.userId(), senderType);


        ChatRoom chatRoom = chatService.getOrCreate(appointment, chatRoomType);

        chatService.sendMessage(
                chatRoom.id(),
                senderType,
                user.userId(),
                content
        );
    }

    private SenderType toSenderType(CurrentUser user) {
        return switch (user.serviceType()) {
            case "patient" -> SenderType.PATIENT;
            case "doctor" -> SenderType.DOCTOR;
            case "hospital" -> SenderType.HOSPITAL;
            default -> throw new IllegalStateException("알 수 없는 serviceType: " + user.serviceType());
        };
    }

    private ChatRoomType resolveChatRoomType(String serviceType) {
        return switch (serviceType) {
            case "doctor" -> ChatRoomType.PATIENT_DOCTOR;
            case "hospital" -> ChatRoomType.PATIENT_HOSPITAL;
            default -> throw new IllegalStateException("채팅 시작 권한 없음");
        };
    }
}
