package com.medicall.storage.db.domain.treatment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentJpaRepository extends JpaRepository<TreatmentEntity, Long> {
    List<TreatmentEntity> findAllByPatientId(Long patientId);
}
