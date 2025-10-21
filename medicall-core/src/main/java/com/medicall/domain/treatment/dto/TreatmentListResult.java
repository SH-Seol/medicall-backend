package com.medicall.domain.treatment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;

public record TreatmentListResult(
        Long treatmentId,
        Patient patient,
        Hospital hospital,
        Doctor doctor,
        LocalDateTime createdAt,
        Long prescriptionId
) {
}
