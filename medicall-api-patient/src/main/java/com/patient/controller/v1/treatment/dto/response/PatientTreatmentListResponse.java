package com.patient.controller.v1.treatment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.treatment.dto.TreatmentListResult;

public record PatientTreatmentListResponse(
        String hospital,
        String doctor,
        LocalDateTime createdAt,
        Long prescriptionId
) {
    public static PatientTreatmentListResponse from(TreatmentListResult result){
        return new PatientTreatmentListResponse(
                result.hospital().name(),
                result.doctor().name(),
                result.createdAt(),
                result.prescriptionId()
        );
    }
}
