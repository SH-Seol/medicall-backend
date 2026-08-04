package com.medicall.storage.db.domain.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorInvitationJpaRepository extends JpaRepository<DoctorInvitationEntity, Long> {

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
