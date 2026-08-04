package com.medicall.domain.department;

import java.util.List;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class SpecialtyReader {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyReader(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public Specialty findById(Long specialtyId) {
        return specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new CoreException(CoreErrorType.SPECIALTY_NOT_FOUND));
    }

    public List<Specialty> findAllByDepartmentId(Long departmentId) {
        return specialtyRepository.findAllByDepartmentId(departmentId);
    }
}
