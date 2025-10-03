package com.patient.controller.v1.hospital.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(name = "병원 상세 조회 요청")
public record PatientHospitalDetailRequest(
        @NotNull
        @Parameter(description = "위도", example = "37.5000")
        double latitude,
        @NotNull
        @Parameter(description = "경도", example = "127.0003")
        double longitude
) {
}
