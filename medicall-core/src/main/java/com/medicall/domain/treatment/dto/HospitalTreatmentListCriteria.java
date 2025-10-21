package com.medicall.domain.treatment.dto;

public record HospitalTreatmentListCriteria(
        Long hospitalId,
        Long cursorId,
        int size
) {
}
