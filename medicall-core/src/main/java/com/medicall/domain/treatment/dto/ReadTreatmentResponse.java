package com.medicall.domain.treatment.dto;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;

public record ReadTreatmentResponse(
        Patient patient,
        Doctor doctor,
        String symptoms,
        String treatment,
        String detailedTreatment,
        Long prescriptionId
) {
}
