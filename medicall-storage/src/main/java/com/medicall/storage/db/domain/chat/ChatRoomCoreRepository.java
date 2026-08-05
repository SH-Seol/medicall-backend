package com.medicall.storage.db.domain.chat;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.chat.ChatRoomRepository;
import com.medicall.domain.chat.ChatRoom;
import com.medicall.domain.chat.dto.ChatRoomSummary;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.storage.db.domain.appointment.AppointmentJpaRepository;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.medicall.storage.db.domain.hospital.HospitalJpaRepository;
import com.medicall.storage.db.domain.patient.PatientJpaRepository;

@Repository
@Transactional(readOnly = true)
public class ChatRoomCoreRepository implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    public ChatRoomCoreRepository(
            ChatRoomJpaRepository chatRoomJpaRepository,
            ChatMessageJpaRepository chatMessageJpaRepository,
            AppointmentJpaRepository appointmentJpaRepository,
            PatientJpaRepository patientJpaRepository,
            DoctorJpaRepository doctorJpaRepository,
            HospitalJpaRepository hospitalJpaRepository
    ) {
        this.chatRoomJpaRepository = chatRoomJpaRepository;
        this.chatMessageJpaRepository = chatMessageJpaRepository;
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.hospitalJpaRepository = hospitalJpaRepository;
    }

    public ChatRoom save(ChatRoom chatRoom) {
        ChatRoomEntity entity = ChatRoomEntity.from(
                chatRoom,
                appointmentJpaRepository.getReferenceById(chatRoom.appointmentId()),
                patientJpaRepository.getReferenceById(chatRoom.patientId()),
                chatRoom.doctorId() != null
                        ? doctorJpaRepository.getReferenceById(chatRoom.doctorId())
                        : null,
                chatRoom.hospitalId() != null
                        ? hospitalJpaRepository.getReferenceById(chatRoom.hospitalId())
                        : null
        );

        return chatRoomJpaRepository.save(entity).toDomainModel();
    }

    public Optional<ChatRoom> findByAppointmentId(
            Long appointmentId,
            ChatRoomType type
    ) {
        return chatRoomJpaRepository
                .findByAppointmentIdAndChatRoomType(appointmentId, type)
                .map(ChatRoomEntity::toDomainModel);
    }

    public List<ChatRoom> findByUserIdAndType(Long userId, SenderType type){
        List<ChatRoomEntity> chatRoomEntities = switch (type){
            case PATIENT -> chatRoomJpaRepository.findAllByPatientId(userId);
            case DOCTOR -> chatRoomJpaRepository.findAllByDoctorId(userId);
            case HOSPITAL -> chatRoomJpaRepository.findAllByHospitalId(userId);
        };

        return chatRoomEntities.stream().map(ChatRoomEntity::toDomainModel).toList();
    }

    /**
     * 목록 화면용 요약 조회.
     * 방 조회(상대방 fetch join) 1회 + 마지막 메시지 1회 + 안읽음 수 1회로 끝낸다.
     */
    public List<ChatRoomSummary> findSummariesByUserIdAndType(Long userId, SenderType type) {
        List<ChatRoomEntity> rooms = switch (type) {
            case PATIENT -> chatRoomJpaRepository.findAllByPatientIdWithCounterpart(userId);
            case DOCTOR -> chatRoomJpaRepository.findAllByDoctorIdWithCounterpart(userId);
            case HOSPITAL -> chatRoomJpaRepository.findAllByHospitalIdWithCounterpart(userId);
        };

        if (rooms.isEmpty()) {
            return List.of();
        }

        List<Long> roomIds = rooms.stream().map(ChatRoomEntity::getId).toList();

        Map<Long, ChatMessageEntity> lastMessages = chatMessageJpaRepository.findLastMessages(roomIds).stream()
                .collect(Collectors.toMap(message -> message.getChatRoom().getId(), Function.identity()));

        Map<Long, Long> unreadCounts = chatMessageJpaRepository.countUnreadByChatRoomIds(roomIds, type).stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        return rooms.stream()
                .map(room -> toSummary(room, type, lastMessages.get(room.getId()), unreadCounts.getOrDefault(room.getId(), 0L)))
                .toList();
    }

    /**
     * 상대방은 내가 누구인지에 따라 달라진다.
     * 환자에게는 의사(또는 병원), 의사·병원에게는 환자가 상대방이다.
     */
    private ChatRoomSummary toSummary(ChatRoomEntity room, SenderType myType,
                                      ChatMessageEntity lastMessage, long unreadCount) {
        Long counterpartId = null;
        String counterpartName = null;
        String counterpartImageUrl = null;

        if (myType == SenderType.PATIENT) {
            if (room.getDoctor() != null) {
                counterpartId = room.getDoctor().getId();
                counterpartName = room.getDoctor().getName();
                counterpartImageUrl = room.getDoctor().getImageUrl();
            } else if (room.getHospital() != null) {
                counterpartId = room.getHospital().getId();
                counterpartName = room.getHospital().getName();
                counterpartImageUrl = room.getHospital().getImageUrl();
            }
        } else {
            counterpartId = room.getPatient().getId();
            counterpartName = room.getPatient().getName();
            counterpartImageUrl = room.getPatient().getImageUrl();
        }

        return new ChatRoomSummary(
                room.getId(),
                room.getAppointment().getId(),
                room.getChatRoomType(),
                counterpartId,
                counterpartName,
                counterpartImageUrl,
                lastMessage != null ? lastMessage.getContent() : null,
                lastMessage != null ? lastMessage.getCreatedAt() : room.getLastMessageTime(),
                unreadCount
        );
    }

    public Optional<ChatRoom> findById(Long chatRoomId){
        return chatRoomJpaRepository.findById(chatRoomId).map(ChatRoomEntity::toDomainModel);
    }
}
