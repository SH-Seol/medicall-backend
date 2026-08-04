package com.hospital.controller.department;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.department.dto.response.HospitalDepartmentResponse;
import com.medicall.domain.department.DepartmentService;

@RestController
@RequestMapping("api/v1/hospital/departments")
public class HospitalDepartmentController implements HospitalDepartmentApiDocs {

    private final DepartmentService departmentService;

    public HospitalDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<HospitalDepartmentResponse> getDepartments() {
        return departmentService.getDepartments().stream()
                .map(HospitalDepartmentResponse::from)
                .toList();
    }
}
