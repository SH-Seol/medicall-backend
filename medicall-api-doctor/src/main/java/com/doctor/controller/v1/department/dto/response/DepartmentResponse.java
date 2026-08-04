package com.doctor.controller.v1.department.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.department.Department;

@Schema(description = "진료과")
public record DepartmentResponse(
        Long id,
        String name
) {
    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(department.id(), department.name());
    }
}
