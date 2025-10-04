package com.medicall.storage.db.domain.prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionMedicineJpaRepository extends JpaRepository<PrescriptionMedicineEntity, Long> {
}
