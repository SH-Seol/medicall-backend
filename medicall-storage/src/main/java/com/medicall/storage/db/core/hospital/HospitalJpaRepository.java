package com.medicall.storage.db.core.hospital;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalJpaRepository extends JpaRepository<HospitalEntity, Long> {
    List<HospitalEntity> findByNameIgnoreCase(String keyword);
}
