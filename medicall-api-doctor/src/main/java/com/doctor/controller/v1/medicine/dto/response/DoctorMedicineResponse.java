package com.doctor.controller.v1.medicine.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.medicine.Medicine;

@Schema(description = "의약품 검색 결과")
public record DoctorMedicineResponse(
        Long medicineId,
        String name,
        String manufacturer,
        @Schema(description = "기본 단위", example = "정")
        String unit
) {
    public static DoctorMedicineResponse from(Medicine medicine) {
        return new DoctorMedicineResponse(
                medicine.id(),
                medicine.name(),
                medicine.manufacturer(),
                medicine.unit()
        );
    }
}
