package com.medicall.domain.treatment;

import com.medicall.domain.prescription.Prescription;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;

public record Treatment(
        Long id,
        Patient patient,
        Doctor doctor,
        String symptoms,
        String treatment,
        String detailedTreatment,
        Prescription prescription
) {
}
