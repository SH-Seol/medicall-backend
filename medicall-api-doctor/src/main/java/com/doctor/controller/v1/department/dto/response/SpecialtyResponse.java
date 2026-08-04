package com.doctor.controller.v1.department.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.department.Specialty;

@Schema(description = "세부 전공")
public record SpecialtyResponse(
        Long id,
        String name,
        Long departmentId,
        String departmentName
) {
    public static SpecialtyResponse from(Specialty specialty) {
        return new SpecialtyResponse(
                specialty.id(),
                specialty.name(),
                specialty.departmentId(),
                specialty.departmentName()
        );
    }
}
