package com.hospital.controller.treatment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.common.dto.DoctorSummaryResponse;
import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;

@Schema(description = "병원 진료 상세 응답")
public record HospitalTreatmentDetailResponse(
        PatientSummaryResponse patient,
        DoctorSummaryResponse doctor,
        String symptoms,
        String treatment,
        String detailedTreatment,
        @Schema(description = "처방전 id (처방 전이면 null)", nullable = true)
        Long prescriptionId
) {
    public static HospitalTreatmentDetailResponse from(TreatmentDetailResult result){
        return new HospitalTreatmentDetailResponse(
                PatientSummaryResponse.from(result.patient()),
                DoctorSummaryResponse.from(result.doctor()),
                result.symptoms(),
                result.treatment(),
                result.detailedTreatment(),
                result.prescriptionId()
        );
    }
}
