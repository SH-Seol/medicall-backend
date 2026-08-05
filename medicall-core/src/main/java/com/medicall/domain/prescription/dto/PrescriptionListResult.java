package com.medicall.domain.prescription.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.medicall.domain.common.enums.PrescriptionStatus;

import com.medicall.domain.prescription.Prescription;

public record PrescriptionListResult(
        Long id,
        String hospital,
        LocalDate createdAt,
        PrescriptionStatus status,
        LocalDateTime dispensedAt
) {
    public static PrescriptionListResult from(Prescription prescription) {
        return new PrescriptionListResult(
                prescription.id(),
                prescription.hospital().name(),
                prescription.date(),
                prescription.status(),
                prescription.dispensedAt()
        );
    }
}
