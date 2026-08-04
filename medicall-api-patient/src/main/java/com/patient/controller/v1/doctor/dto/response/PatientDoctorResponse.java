package com.patient.controller.v1.doctor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.doctor.dto.DoctorResult;

@Schema(description = "의사 정보")
public record PatientDoctorResponse(
        @Schema(description = "의사 id")
        Long doctorId,

        @Schema(description = "의사명")
        String doctor,

        @Schema(description = "소속 병원 id (예약 생성 시 사용)")
        Long hospitalId,

        @Schema(description = "소속 병원명")
        String hospital,

        @Schema(description = "진료과")
        String department,

        @Schema(description = "세부 전공")
        String specialty,

        String introduction,
        String imageUrl
) {
    public static PatientDoctorResponse from(DoctorResult result) {
        return new PatientDoctorResponse(
                result.id(),
                result.name(),
                result.hospitalId(),
                result.hospitalName(),
                result.department(),
                result.specialty(),
                result.introduction(),
                result.imageUrl()
        );
    }
}
