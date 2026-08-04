package com.hospital.controller.hospital.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "병원 진료과 등록 요청 (전달한 목록으로 전체 교체)")
public record UpdateHospitalDepartmentsRequest(
        @Schema(description = "진료과 id 목록", example = "[1, 2, 5]")
        @NotEmpty(message = "진료과는 최소 1개 이상이어야 합니다.")
        List<Long> departmentIds
) {
}
