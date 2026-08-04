package com.hospital.controller.hospital.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.medicall.domain.hospital.dto.HospitalProfileUpdate;

@Schema(description = "병원 내 정보 수정 요청 (null인 항목은 수정하지 않습니다.)")
public record UpdateHospitalProfileRequest(
        @Schema(description = "병원명", example = "메디콜 내과")
        @Size(min = 1, max = 50)
        String name,

        @Schema(description = "대표 전화번호", example = "02-123-4567")
        @Pattern(regexp = "^0\\d{1,2}-?\\d{3,4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String telephoneNumber,

        @Schema(description = "병원 대표 이미지 url")
        String imageUrl
) {
    public HospitalProfileUpdate toProfileUpdate() {
        return new HospitalProfileUpdate(name, telephoneNumber, imageUrl);
    }
}
