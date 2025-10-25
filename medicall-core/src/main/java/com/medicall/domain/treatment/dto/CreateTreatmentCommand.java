package com.medicall.domain.treatment.dto;

public record CreateTreatmentCommand(
        Long patientId,
        String symptoms,
        String treatment,
        String detailedTreatment
) {
}
