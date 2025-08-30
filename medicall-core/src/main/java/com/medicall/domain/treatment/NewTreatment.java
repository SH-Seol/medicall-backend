package com.medicall.domain.treatment;

public record NewTreatment(
        Long patientId,
        Long doctorId,
        String symptoms,
        String treatment,
        String detailedTreatment
) {
}
