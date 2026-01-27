package com.medicall.storage.db.domain.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.chat.ChatRoomRepository;
import com.medicall.domain.chat.ChatRoom;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.domain.common.enums.SenderType;
import com.medicall.storage.db.domain.appointment.AppointmentJpaRepository;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.medicall.storage.db.domain.hospital.HospitalJpaRepository;
import com.medicall.storage.db.domain.patient.PatientJpaRepository;

@Repository
public class ChatRoomCoreRepository implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    public ChatRoomCoreRepository(
            ChatRoomJpaRepository chatRoomJpaRepository,
            AppointmentJpaRepository appointmentJpaRepository,
            PatientJpaRepository patientJpaRepository,
            DoctorJpaRepository doctorJpaRepository,
            HospitalJpaRepository hospitalJpaRepository
    ) {
        this.chatRoomJpaRepository = chatRoomJpaRepository;
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

}
