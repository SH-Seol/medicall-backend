package com.doctor.controller.v1.prescription.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

import com.medicall.common.dto.DoctorSummaryResponse;
import com.medicall.common.dto.HospitalSummaryResponse;
import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.common.dto.PrescriptionMedicineResponse;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

@Schema(description = "의사 처방전 상세 응답")
public record DoctorPrescriptionDetailResponse(
        PatientSummaryResponse patient,
        List<PrescriptionMedicineResponse> medicines,
        HospitalSummaryResponse hospital,
        DoctorSummaryResponse doctor,
        @Schema(description = "연결된 진료 id", nullable = true)
        Long treatmentId,
        LocalDate date
) {
    public static DoctorPrescriptionDetailResponse from(PrescriptionDetailResult result){
        return new DoctorPrescriptionDetailResponse(
                PatientSummaryResponse.from(result.patient()),
                PrescriptionMedicineResponse.listFrom(result.medicines()),
                HospitalSummaryResponse.from(result.hospital()),
                DoctorSummaryResponse.from(result.doctor()),
                result.treatment() != null ? result.treatment().id() : null,
                result.date()
        );
    }
}
