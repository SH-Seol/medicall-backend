package com.hospital.controller.hospital.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.hospital.dto.HospitalSetupStatus;

@Schema(description = "병원 온보딩 진행 상태")
public record HospitalSetupStatusResponse(
        @Schema(description = "주소 등록 여부")
        boolean addressRegistered,

        @Schema(description = "진료과 등록 여부")
        boolean departmentsRegistered,

        @Schema(description = "업무 시간 등록 여부")
        boolean operatingTimesRegistered,

        @Schema(description = "온보딩 완료 여부")
        boolean completed
) {
    public static HospitalSetupStatusResponse from(HospitalSetupStatus status) {
        return new HospitalSetupStatusResponse(
                status.addressRegistered(),
                status.departmentsRegistered(),
                status.operatingTimesRegistered(),
                status.completed()
        );
    }
}
