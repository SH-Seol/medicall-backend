package com.medicall.domain.treatment.dto;

public record PatientTreatmentListCriteria(
        Long patientId,
        Long cursorId,
        int size
) {
}
