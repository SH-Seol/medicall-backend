package com.hospital.controller.treatment.dto.response;

import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;

public record HospitalTreatmentDetailResponse(
        Patient patient,
        Doctor doctor,
        String symptoms,
        String treatment,
        String detailedTreatment,
        Long prescriptionId
) {
    public static HospitalTreatmentDetailResponse from(TreatmentDetailResult result){
        return new HospitalTreatmentDetailResponse(
                result.patient(),
                result.doctor(),
                result.symptoms(),
                result.treatment(),
                result.detailedTreatment(),
                result.prescriptionId()
        );
    }
}
