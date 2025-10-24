package com.doctor.controller.v1.treatment.dto.response;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;

public record DoctorTreatmentDetailResponse(
        Patient patient,
        String symptoms,
        String treatment,
        String detailedTreatment,
        Long prescriptionId
) {
    public static DoctorTreatmentDetailResponse from(TreatmentDetailResult result){
        return new DoctorTreatmentDetailResponse(
                result.patient(),
                result.symptoms(),
                result.treatment(),
                result.detailedTreatment(),
                result.prescriptionId()
        );
    }
}
