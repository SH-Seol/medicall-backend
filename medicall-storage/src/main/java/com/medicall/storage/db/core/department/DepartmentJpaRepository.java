package com.medicall.storage.db.core.department;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentJpaRepository extends JpaRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findById(Long id);
}
