package com.medicall.storage.db.domain.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medicall.domain.common.enums.ChatRoomType;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    Optional<ChatRoomEntity> findByAppointmentIdAndChatRoomType(
            Long appointmentId,
            ChatRoomType chatRoomType
    );

    // DoctorEntity.hospital이 즉시 로딩이라 함께 가져오지 않으면 방 개수만큼 추가 쿼리가 나간다.
    @Query("SELECT r FROM ChatRoomEntity r LEFT JOIN FETCH r.doctor d LEFT JOIN FETCH d.hospital LEFT JOIN FETCH r.hospital WHERE r.patient.id = :userId ORDER BY r.id DESC")
    List<ChatRoomEntity> findAllByPatientIdWithCounterpart(@Param("userId") Long userId);

    @Query("SELECT r FROM ChatRoomEntity r JOIN FETCH r.patient WHERE r.doctor.id = :userId ORDER BY r.id DESC")
    List<ChatRoomEntity> findAllByDoctorIdWithCounterpart(@Param("userId") Long userId);

    @Query("SELECT r FROM ChatRoomEntity r JOIN FETCH r.patient WHERE r.hospital.id = :userId ORDER BY r.id DESC")
    List<ChatRoomEntity> findAllByHospitalIdWithCounterpart(@Param("userId") Long userId);

    List<ChatRoomEntity> findAllByPatientId(Long patientId);
    List<ChatRoomEntity> findAllByDoctorId(Long doctorId);
    List<ChatRoomEntity> findAllByHospitalId(Long hospitalId);
}
