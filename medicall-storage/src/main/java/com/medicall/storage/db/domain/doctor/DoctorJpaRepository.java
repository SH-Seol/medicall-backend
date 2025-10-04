package com.medicall.storage.db.domain.doctor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Long> {
    @Query("SELECT d FROM DoctorEntity d JOIN FETCH d.hospital WHERE d.id = :doctorId")
    DoctorEntity findByIdWithHospital(@Param("doctorId") Long doctorId);

    @Query("SELECT d FROM DoctorEntity d LEFT JOIN FETCH d.hospital WHERE d.id = :doctorId")
    DoctorEntity findByIdWithOptionalHospital(@Param("doctorId") Long doctorId);

    Optional<DoctorEntity> findByOauthIdAndOauthProvider(String oauthId, String oauthProvider);
}
