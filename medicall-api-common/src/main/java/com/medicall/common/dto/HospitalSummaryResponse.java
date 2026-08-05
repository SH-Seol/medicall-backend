package com.medicall.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.hospital.Hospital;

@Schema(description = "병원 요약 정보")
public record HospitalSummaryResponse(
        Long hospitalId,
        String name,
        String telephoneNumber,
        String imageUrl
) {
    public static HospitalSummaryResponse from(Hospital hospital) {
        if (hospital == null) {
            return null;
        }
        return new HospitalSummaryResponse(
                hospital.id(),
                hospital.name(),
                hospital.telephoneNumber(),
                hospital.imageUrl()
        );
    }
}
