package com.patient.controller.v1.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.medicall.domain.address.Address;

@Schema(description = "환자 주소 등록·수정 요청 (카카오 주소 검색 결과)")
public record PatientAddressRequest(
        @Schema(description = "우편번호(5자리)", example = "06236")
        @NotBlank @Size(min = 5, max = 5)
        String zoneCode,

        @Schema(description = "도로명 주소", example = "서울 강남구 테헤란로 152")
        @NotBlank
        String roadAddress,

        @Schema(description = "지번 주소")
        String jibunAddress,

        @Schema(description = "상세 주소", example = "3층 301호")
        String detailAddress,

        @Schema(description = "건물명")
        String buildingName,

        @Schema(description = "경도", example = "127.036508")
        @NotNull
        Double longitude,

        @Schema(description = "위도", example = "37.500693")
        @NotNull
        Double latitude
) {
    public Address toAddress() {
        return new Address(null, zoneCode, roadAddress, jibunAddress, detailAddress,
                buildingName, longitude, latitude, false);
    }
}
