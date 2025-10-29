package com.medicall.domain.prescription.dto;

import java.time.LocalDate;

public record CreatePrescriptionResult(
        Long prescriptionId,
        String patient,
        LocalDate createdAt
) {
}
