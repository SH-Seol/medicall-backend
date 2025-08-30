package com.medicall.storage.db.core.prescription;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionMedicineJpaRepository extends JpaRepository<PrescriptionMedicineEntity, Long> {
}
