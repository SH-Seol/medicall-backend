package com.patient.controller.v1.prescription.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.medicall.domain.prescription.dto.PrescriptionListResult;

public record PatientPrescriptionListResponse(
        Long prescriptionId,
        String hospital,
        LocalDate createdAt,
        String status,
        LocalDateTime dispensedAt
) {
    public static PatientPrescriptionListResponse from(PrescriptionListResult prescription) {
        return new PatientPrescriptionListResponse(
                prescription.id(),
                prescription.hospital(),
                prescription.createdAt(),
                prescription.status() != null ? prescription.status().name() : null,
                prescription.dispensedAt()
        );
    }
}
