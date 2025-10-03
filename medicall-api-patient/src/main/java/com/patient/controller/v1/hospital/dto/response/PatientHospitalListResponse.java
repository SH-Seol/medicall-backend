package com.patient.controller.v1.hospital.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.hospital.dto.HospitalSearchResult;

@Schema(description = "환자 병원 목록 조회 응답")
public record PatientHospitalListResponse(
        @Schema(description = "병원 ID", example = "120")
        Long hospitalId,
        @Schema(description = "병원 이름", example = "메디콜정형외과")
        String name,
        @Schema(description = "병원 대표 이미지")
        String imageUrl,
        @Schema(description = "환자와 병원 간 거리(km)", example = "5.10")
        double distance,
        @Schema(description = "병원 영업 여부", example = "OPEN")
        String businessStatus
) {
    /**
     * core 모듈 내 dto를 api dto로 전환
     */
    public static PatientHospitalListResponse from(HospitalSearchResult result){
        return new PatientHospitalListResponse(
                result.id(),
                result.name(),
                result.imageUrl(),
                result.distance(),
                result.businessStatus()
        );
    }
}
