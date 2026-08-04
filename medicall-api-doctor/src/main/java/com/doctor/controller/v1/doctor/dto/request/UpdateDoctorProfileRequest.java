package com.doctor.controller.v1.doctor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import com.medicall.domain.doctor.dto.DoctorProfileUpdate;

@Schema(description = "의사 내 정보 수정 요청 (null인 항목은 수정하지 않습니다.)")
public record UpdateDoctorProfileRequest(
        @Schema(description = "이름", example = "김의사")
        @Size(min = 1, max = 30)
        String name,

        @Schema(description = "소개", example = "10년 경력의 내과 전문의입니다.")
        @Size(max = 500)
        String introduction,

        @Schema(description = "프로필 이미지 url")
        String imageUrl,

        @Schema(description = "진료과 id", example = "1")
        @Positive
        Long departmentId,

        @Schema(description = "세부 전공 id (진료과에 속한 값이어야 함)", example = "3")
        @Positive
        Long specialtyId
) {
    public DoctorProfileUpdate toProfileUpdate() {
        return new DoctorProfileUpdate(name, introduction, imageUrl, departmentId, specialtyId);
    }
}
