package com.hospital.controller.doctor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.doctor.dto.DoctorResult;

@Schema(description = "병원 소속 의사 정보")
public record HospitalDoctorDetailResponse(
        Long id,
        String name,
        String introduction,
        String imageUrl,
        Long departmentId,
        String departmentName,
        Long specialtyId,
        String specialtyName
) {
    public static HospitalDoctorDetailResponse from(DoctorResult result) {
        return new HospitalDoctorDetailResponse(
                result.id(),
                result.name(),
                result.introduction(),
                result.imageUrl(),
                result.departmentId(),
                result.department(),
                result.specialtyId(),
                result.specialty()
        );
    }
}
