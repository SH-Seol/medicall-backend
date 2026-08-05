package com.doctor.controller.v1.treatment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;

@Schema(description = "의사 진료 상세 응답")
public record DoctorTreatmentDetailResponse(
        PatientSummaryResponse patient,
        String symptoms,
        String treatment,
        String detailedTreatment,
        @Schema(description = "처방전 id (처방 전이면 null)", nullable = true)
        Long prescriptionId
) {
    public static DoctorTreatmentDetailResponse from(TreatmentDetailResult result){
        return new DoctorTreatmentDetailResponse(
                PatientSummaryResponse.from(result.patient()),
                result.symptoms(),
                result.treatment(),
                result.detailedTreatment(),
                result.prescriptionId()
        );
    }
}
