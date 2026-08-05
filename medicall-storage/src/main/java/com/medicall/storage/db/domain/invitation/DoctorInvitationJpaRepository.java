package com.medicall.storage.db.domain.invitation;

import java.util.List;

import com.medicall.storage.db.domain.doctor.DoctorEntity;
import java.util.Optional;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorInvitationJpaRepository extends JpaRepository<DoctorInvitationEntity, Long> {

    /**
     * PENDING 상태인 초대만 수락 처리한다.
     * 동시에 두 의사가 같은 코드를 수락하면 한 쪽만 1을 돌려받는다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE DoctorInvitationEntity i
            SET i.status = com.medicall.domain.invitation.InvitationStatus.ACCEPTED,
                i.acceptedDoctor = :doctor,
                i.acceptedAt = :acceptedAt
            WHERE i.id = :invitationId
              AND i.status = com.medicall.domain.invitation.InvitationStatus.PENDING
            """)
    int acceptIfPending(@Param("invitationId") Long invitationId,
                        @Param("doctor") DoctorEntity doctor,
                        @Param("acceptedAt") LocalDateTime acceptedAt);

    /**
     * PENDING 상태인 초대만 취소한다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE DoctorInvitationEntity i
            SET i.status = com.medicall.domain.invitation.InvitationStatus.CANCELED
            WHERE i.id = :invitationId
              AND i.status = com.medicall.domain.invitation.InvitationStatus.PENDING
            """)
    int cancelIfPending(@Param("invitationId") Long invitationId);

    @Query("""
            SELECT i FROM DoctorInvitationEntity i
            JOIN FETCH i.hospital
            LEFT JOIN FETCH i.acceptedDoctor
            WHERE i.code = :code
            """)
    Optional<DoctorInvitationEntity> findByCodeWithDetails(@Param("code") String code);

    @Query("""
            SELECT i FROM DoctorInvitationEntity i
            JOIN FETCH i.hospital
            LEFT JOIN FETCH i.acceptedDoctor
            WHERE i.hospital.id = :hospitalId
            ORDER BY i.id DESC
            """)
    List<DoctorInvitationEntity> findAllByHospitalIdWithDetails(@Param("hospitalId") Long hospitalId);
}
