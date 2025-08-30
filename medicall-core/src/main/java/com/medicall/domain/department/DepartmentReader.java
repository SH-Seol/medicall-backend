package com.medicall.domain.department;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class DepartmentReader {

    private final DepartmentRepository departmentRepository;

    public DepartmentReader(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new CoreException(CoreErrorType.DEPARTMENT_NOT_FOUND));
    }
}
