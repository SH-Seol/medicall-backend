package com.doctor.controller.v1.prescription.dto.request;

import java.util.List;

import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.prescription.dto.CreatePrescriptionCommand;

public record CreateDoctorPrescriptionRequest(
        Long treatmentId,
        List<PrescriptionMedicine> medicines
) {
    public CreatePrescriptionCommand toCommand(){
        return new CreatePrescriptionCommand(treatmentId, medicines);
    }
}
