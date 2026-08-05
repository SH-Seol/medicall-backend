package com.medicall.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.doctor.Doctor;

@Schema(description = "의사 요약 정보")
public record DoctorSummaryResponse(
        Long doctorId,
        String name,
        String department,
        String specialty,
        String imageUrl
) {
    public static DoctorSummaryResponse from(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        return new DoctorSummaryResponse(
                doctor.id(),
                doctor.name(),
                doctor.department() != null ? doctor.department().name() : null,
                doctor.specialty() != null ? doctor.specialty().name() : null,
                doctor.imageUrl()
        );
    }
}
