package com.doctor.controller.v1.prescription.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;
import com.medicall.domain.treatment.Treatment;

public record DoctorPrescriptionDetailResponse(
        Patient patient,
        List<PrescriptionMedicine> medicines,
        Hospital hospital,
        Doctor doctor,
        Treatment treatment,
        LocalDate date
) {
    public static DoctorPrescriptionDetailResponse from(PrescriptionDetailResult result){
        return new DoctorPrescriptionDetailResponse(
                result.patient(),
                result.medicines(),
                result.hospital(),
                result.doctor(),
                result.treatment(),
                result.date()
        );
    }
}
