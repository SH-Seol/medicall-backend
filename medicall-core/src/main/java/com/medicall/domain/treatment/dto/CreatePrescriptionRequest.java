package com.medicall.domain.treatment.dto;

public record CreatePrescriptionRequest(
        Long patientId,
        String symptoms,
        String treatment,
        String detailedTreatment
) {
}
