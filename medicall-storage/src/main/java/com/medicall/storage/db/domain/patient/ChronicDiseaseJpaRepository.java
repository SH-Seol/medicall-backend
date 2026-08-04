package com.medicall.storage.db.domain.patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChronicDiseaseJpaRepository extends JpaRepository<ChronicDiseaseEntity, Long> {
    List<ChronicDiseaseEntity> findAllByNameIn(List<String> names);
}
