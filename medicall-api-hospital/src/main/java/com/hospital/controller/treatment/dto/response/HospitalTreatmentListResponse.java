package com.hospital.controller.treatment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.treatment.dto.TreatmentListResult;

public record HospitalTreatmentListResponse(
        Long treatmentId,
        Patient patient,
        Doctor doctor,
        LocalDateTime createdAt,
        Long prescriptionId
) {
    public static HospitalTreatmentListResponse from(TreatmentListResult result){
        return new HospitalTreatmentListResponse(
                result.treatmentId(),
                result.patient(),
                result.doctor(),
                result.createdAt(),
                result.prescriptionId()
        );
    }
}
