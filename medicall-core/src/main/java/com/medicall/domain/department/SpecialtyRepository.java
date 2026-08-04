package com.medicall.domain.department;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository {
    Optional<Specialty> findById(Long id);
    List<Specialty> findAllByDepartmentId(Long departmentId);
}
