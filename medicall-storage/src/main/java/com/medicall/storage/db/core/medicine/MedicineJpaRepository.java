package com.medicall.storage.db.core.medicine;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineJpaRepository extends JpaRepository<MedicineEntity, Long> {
    List<MedicineEntity> findTop5ByNameContaining(String keyword);
}
