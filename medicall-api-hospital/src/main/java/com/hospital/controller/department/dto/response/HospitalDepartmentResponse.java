package com.hospital.controller.department.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.department.Department;

@Schema(description = "진료과")
public record HospitalDepartmentResponse(
        Long id,
        String name
) {
    public static HospitalDepartmentResponse from(Department department) {
        return new HospitalDepartmentResponse(department.id(), department.name());
    }
}
