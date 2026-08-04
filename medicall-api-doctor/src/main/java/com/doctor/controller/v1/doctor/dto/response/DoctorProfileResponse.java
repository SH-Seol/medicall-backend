package com.doctor.controller.v1.doctor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.doctor.dto.DoctorResult;

@Schema(description = "의사 내 정보 응답")
public record DoctorProfileResponse(
        Long id,
        String name,
        String hospitalName,
        String introduction,
        String imageUrl,
        Long departmentId,
        String departmentName,
        Long specialtyId,
        String specialtyName
) {
    public static DoctorProfileResponse from(DoctorResult result) {
        return new DoctorProfileResponse(
                result.id(),
                result.name(),
                result.hospitalName(),
                result.introduction(),
                result.imageUrl(),
                result.departmentId(),
                result.department(),
                result.specialtyId(),
                result.specialty()
        );
    }
}
