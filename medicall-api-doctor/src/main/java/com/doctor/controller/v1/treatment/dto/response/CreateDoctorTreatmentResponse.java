package com.doctor.controller.v1.treatment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.treatment.dto.CreateTreatmentResult;

public record CreateDoctorTreatmentResponse(
        Long treatmentId,
        String patient,
        LocalDateTime createTime
) {
    public static CreateDoctorTreatmentResponse from(CreateTreatmentResult result) {
        return new CreateDoctorTreatmentResponse(
                result.treatmentId(),
                result.patient(),
                result.createTime()
        );
    }
}
