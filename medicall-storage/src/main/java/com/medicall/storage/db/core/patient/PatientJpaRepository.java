package com.medicall.storage.db.core.patient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    Optional<PatientEntity> findByOauthIdAndOauthProvider(String oauthId, String oauthProvider);
}
