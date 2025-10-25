package com.doctor.controller.v1.treatment.dto.request;

import com.medicall.domain.treatment.dto.CreateTreatmentCommand;

public record CreateDoctorTreatmentRequest(
        Long patientId,
        String symptoms,
        String treatment,
        String detailedTreatment
) {
    public CreateTreatmentCommand toCommand() {
        return new CreateTreatmentCommand(patientId, symptoms, treatment, detailedTreatment);
    }
}
