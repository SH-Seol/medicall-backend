package com.medicall.domain.treatment;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.prescription.Prescription;

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
