package com.medicall.domain.prescription.dto;

import java.time.LocalDate;

import com.medicall.domain.prescription.Prescription;

public record PrescriptionListResult(
        Long id,
        String hospital,
        LocalDate createdAt
) {
    public static PrescriptionListResult from(Prescription prescription) {
        return new PrescriptionListResult(
                prescription.id(),
                prescription.hospital().name(),
                prescription.date()
        );
    }
}
