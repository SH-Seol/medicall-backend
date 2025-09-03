package com.medicall.domain.department;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository {
    Optional<Department> findById(Long id);
}
