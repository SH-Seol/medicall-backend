package com.medicall.domain.prescription.dto;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.treatment.Treatment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.medicall.domain.common.enums.PrescriptionStatus;
import com.medicall.domain.prescription.Prescription;
import java.util.List;

public record PrescriptionDetailResult(
        Long id,
        Patient patient,
        List<PrescriptionMedicine> medicines,
        Hospital hospital,
        Doctor doctor,
        Treatment treatment,
        LocalDate date,
        PrescriptionStatus status,
        LocalDateTime dispensedAt
) {
    public static PrescriptionDetailResult from(Prescription prescription){
        return new PrescriptionDetailResult(
                prescription.id(),
                prescription.patient(),
                prescription.medicines(),
                prescription.hospital(),
                prescription.doctor(),
                prescription.treatment(),
                prescription.date(),
                prescription.status(),
                prescription.dispensedAt()
        );
    }
}
