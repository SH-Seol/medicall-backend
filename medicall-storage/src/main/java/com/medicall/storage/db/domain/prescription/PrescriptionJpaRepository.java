package com.medicall.storage.db.domain.prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionJpaRepository extends JpaRepository<PrescriptionEntity, Long> {
}
