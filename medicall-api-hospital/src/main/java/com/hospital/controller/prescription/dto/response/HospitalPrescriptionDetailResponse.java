package com.hospital.controller.prescription.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

public record HospitalPrescriptionDetailResponse(
        List<PrescriptionMedicine> medicines,
        String hospital,
        String doctor,
        LocalDate date
) {
    public static HospitalPrescriptionDetailResponse from(PrescriptionDetailResult result){
        return new HospitalPrescriptionDetailResponse(
                result.medicines(),
                result.hospital().name(),
                result.doctor().name(),
                result.date()
        );
    }
}
