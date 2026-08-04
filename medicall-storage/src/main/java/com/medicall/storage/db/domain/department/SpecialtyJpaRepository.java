package com.medicall.storage.db.domain.department;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialtyJpaRepository extends JpaRepository<SpecialtyEntity, Long> {

    @Query("SELECT s FROM SpecialtyEntity s JOIN FETCH s.department WHERE s.id = :id")
    Optional<SpecialtyEntity> findByIdWithDepartment(@Param("id") Long id);

    @Query("SELECT s FROM SpecialtyEntity s JOIN FETCH s.department WHERE s.department.id = :departmentId ORDER BY s.name")
    List<SpecialtyEntity> findAllByDepartmentIdWithDepartment(@Param("departmentId") Long departmentId);
}
