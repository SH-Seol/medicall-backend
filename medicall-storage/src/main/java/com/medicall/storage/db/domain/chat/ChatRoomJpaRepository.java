package com.medicall.storage.db.domain.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medicall.domain.common.enums.ChatRoomType;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findByAppointmentIdAndChatRoomType(
            Long appointmentId,
            ChatRoomType chatRoomType
    );

    List<ChatRoomEntity> findAllByPatientId(Long patientId);
    List<ChatRoomEntity> findAllByDoctorId(Long doctorId);
    List<ChatRoomEntity> findAllByHospitalId(Long hospitalId);
}
