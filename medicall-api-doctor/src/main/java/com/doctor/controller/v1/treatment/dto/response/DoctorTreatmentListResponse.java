package com.doctor.controller.v1.treatment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.treatment.dto.TreatmentListResult;

public record DoctorTreatmentListResponse(
        Long treatmentId,
        String patient,
        LocalDateTime createdAt,
        Long prescriptionId
) {
    public static DoctorTreatmentListResponse from(TreatmentListResult result){
        return new DoctorTreatmentListResponse(
                result.treatmentId(),
                result.patient().name(),
                result.createdAt(),
                result.prescriptionId()
        );
    }
}
