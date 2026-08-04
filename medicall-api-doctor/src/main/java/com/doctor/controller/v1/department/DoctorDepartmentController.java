package com.doctor.controller.v1.department;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.department.dto.response.DepartmentResponse;
import com.doctor.controller.v1.department.dto.response.SpecialtyResponse;
import com.medicall.domain.department.DepartmentService;

@RestController
@RequestMapping("api/v1/doctor/departments")
public class DoctorDepartmentController implements DoctorDepartmentApiDocs {

    private final DepartmentService departmentService;

    public DoctorDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentResponse> getDepartments() {
        return departmentService.getDepartments().stream()
                .map(DepartmentResponse::from)
                .toList();
    }

    @GetMapping("/{departmentId}/specialties")
    public List<SpecialtyResponse> getSpecialties(@PathVariable("departmentId") Long departmentId) {
        return departmentService.getSpecialties(departmentId).stream()
                .map(SpecialtyResponse::from)
                .toList();
    }
}
