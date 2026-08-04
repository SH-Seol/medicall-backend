package com.medicall.domain.department;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 온보딩 시 진료과·세부전공 선택 목록을 제공한다.
 */
@Service
public class DepartmentService {

    private final DepartmentReader departmentReader;
    private final SpecialtyReader specialtyReader;

    public DepartmentService(DepartmentReader departmentReader, SpecialtyReader specialtyReader) {
        this.departmentReader = departmentReader;
        this.specialtyReader = specialtyReader;
    }

    @Transactional(readOnly = true)
    public List<Department> getDepartments() {
        return departmentReader.findAll();
    }

    @Transactional(readOnly = true)
    public List<Specialty> getSpecialties(Long departmentId) {
        departmentReader.findById(departmentId);

        return specialtyReader.findAllByDepartmentId(departmentId);
    }
}
