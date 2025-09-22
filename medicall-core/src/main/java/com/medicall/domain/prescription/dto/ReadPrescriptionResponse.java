package com.medicall.domain.prescription.dto;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.treatment.Treatment;
import java.time.LocalDate;
import java.util.List;

public record ReadPrescriptionResponse(
        Long id,
        Patient patient,
        List<PrescriptionMedicine> medicines,
        Hospital hospital,
        Doctor doctor,
        Treatment treatment,
        LocalDate date
) {
}
