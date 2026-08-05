package com.patient.controller.v1.prescription.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.medicall.common.dto.PrescriptionMedicineResponse;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

@Schema(description = "처방전 상세 응답")
public record PatientPrescriptionDetailResponse(
        Long prescriptionId,
        List<PrescriptionMedicineResponse> medicines,
        String hospital,
        String doctor,
        LocalDate date,
        @Schema(description = "조제 상태", example = "ISSUED", allowableValues = {"ISSUED", "DISPENSED"})
        String status,
        @Schema(description = "조제 완료 시각 (조제 전이면 null)", nullable = true)
        LocalDateTime dispensedAt
) {
    public static PatientPrescriptionDetailResponse from(PrescriptionDetailResult result) {
        return new PatientPrescriptionDetailResponse(
                result.id(),
                PrescriptionMedicineResponse.listFrom(result.medicines()),
                result.hospital().name(),
                result.doctor().name(),
                result.date(),
                result.status() != null ? result.status().name() : null,
                result.dispensedAt()
        );
    }
}
