package com.patient.controller.v1.hospital.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import org.springdoc.core.annotations.ParameterObject;

import com.medicall.domain.hospital.dto.HospitalSearchByNameCriteria;

@ParameterObject
@Schema(description = "병원 이름 조회")
public record PatientHospitalSearchByNameRequest(
        @NotNull
        @Parameter(description = "검색어", example = "경희")
        String keyword,
        @NotNull
        @Parameter(description = "위도", example = "37.5000")
        double latitude,
        @NotNull
        @Parameter(description = "경도", example = "127.0003")
        double longitude,
        @Parameter(description = "커서 ID (null인 경우 첫 페이지)", example = "10")
        Long cursorId,
        @Parameter(description = "페이지 크기 (1-100)", example = "25")
        int size
) {
        public HospitalSearchByNameCriteria toCriteria(Long patientId) {
                return new HospitalSearchByNameCriteria(
                        patientId,
                        keyword,
                        cursorId,
                        size,
                        latitude,
                        longitude
                );
        }
}
