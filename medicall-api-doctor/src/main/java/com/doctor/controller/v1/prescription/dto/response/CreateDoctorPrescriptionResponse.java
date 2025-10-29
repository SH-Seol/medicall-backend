package com.doctor.controller.v1.prescription.dto.response;


import java.time.LocalDate;

import com.medicall.domain.prescription.dto.CreatePrescriptionResult;

public record CreateDoctorPrescriptionResponse(
        Long prescriptionId,
        String patient,
        LocalDate createdAt
) {
    public static CreateDoctorPrescriptionResponse from(CreatePrescriptionResult result){
        return new CreateDoctorPrescriptionResponse(
                result.prescriptionId(),
                result.patient(),
                result.createdAt()
        );
    }
}
