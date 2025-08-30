package com.medicall.domain.department;

import java.util.Optional;

public interface DepartmentRepository {
    Optional<Department> findById(Long id);
}
