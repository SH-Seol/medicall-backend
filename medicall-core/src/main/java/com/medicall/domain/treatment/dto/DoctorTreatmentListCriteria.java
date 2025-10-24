package com.medicall.domain.treatment.dto;

public record DoctorTreatmentListCriteria(
        Long doctorId,
        Long patientId,
        Long cursorId,
        int size
) {
}
