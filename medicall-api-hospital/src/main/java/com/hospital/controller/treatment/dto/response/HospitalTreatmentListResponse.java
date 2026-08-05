package com.hospital.controller.treatment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.common.dto.DoctorSummaryResponse;
import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.domain.treatment.dto.TreatmentListResult;

@Schema(description = "병원 진료 목록 응답")
public record HospitalTreatmentListResponse(
        Long treatmentId,
        PatientSummaryResponse patient,
        DoctorSummaryResponse doctor,
        LocalDateTime createdAt,
        @Schema(description = "처방전 id (처방 전이면 null)", nullable = true)
        Long prescriptionId
) {
    public static HospitalTreatmentListResponse from(TreatmentListResult result){
        return new HospitalTreatmentListResponse(
                result.treatmentId(),
                PatientSummaryResponse.from(result.patient()),
                DoctorSummaryResponse.from(result.doctor()),
                result.createdAt(),
                result.prescriptionId()
        );
    }
}
