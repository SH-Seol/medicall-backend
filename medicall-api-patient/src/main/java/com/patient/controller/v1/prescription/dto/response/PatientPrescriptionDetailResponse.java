package com.patient.controller.v1.prescription.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

public record PatientPrescriptionDetailResponse(
        List<PrescriptionMedicine> medicines,
        String hospital,
        String doctor,
        LocalDate date
) {
    public static PatientPrescriptionDetailResponse from(PrescriptionDetailResult result) {
        return new PatientPrescriptionDetailResponse(
                result.medicines(),
                result.hospital().name(),
                result.doctor().name(),
                result.date()
        );
    }
}
