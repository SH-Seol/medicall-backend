package com.patient.controller.v1.treatment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.common.dto.DoctorSummaryResponse;
import com.medicall.common.dto.HospitalSummaryResponse;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;

@Schema(description = "환자 진료 상세 응답")
public record PatientTreatmentDetailResponse(
        HospitalSummaryResponse hospital,
        DoctorSummaryResponse doctor,
        @Schema(description = "증상")
        String symptoms,
        @Schema(description = "처치 내용")
        String treatment,
        @Schema(description = "상세 소견")
        String detailedTreatment,
        @Schema(description = "처방전 id (처방 전이면 null)", nullable = true)
        Long prescriptionId
) {
    public static PatientTreatmentDetailResponse from(TreatmentDetailResult result) {
        return new PatientTreatmentDetailResponse(
                HospitalSummaryResponse.from(result.hospital()),
                DoctorSummaryResponse.from(result.doctor()),
                result.symptoms(),
                result.treatment(),
                result.detailedTreatment(),
                result.prescriptionId()
        );
    }
}
