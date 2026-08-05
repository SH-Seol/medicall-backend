package com.medicall.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import com.medicall.domain.prescription.PrescriptionMedicine;

@Schema(description = "처방 의약품")
public record PrescriptionMedicineResponse(
        Long medicineId,
        String name,
        String manufacturer,
        double dosage,
        String dosageUnit,
        int quantity,
        String frequency,
        String instruction
) {
    public static PrescriptionMedicineResponse from(PrescriptionMedicine prescriptionMedicine) {
        return new PrescriptionMedicineResponse(
                prescriptionMedicine.medicine().id(),
                prescriptionMedicine.medicine().name(),
                prescriptionMedicine.medicine().manufacturer(),
                prescriptionMedicine.dosage(),
                prescriptionMedicine.dosageUnit(),
                prescriptionMedicine.quantity(),
                prescriptionMedicine.frequency(),
                prescriptionMedicine.instruction()
        );
    }

    public static List<PrescriptionMedicineResponse> listFrom(List<PrescriptionMedicine> medicines) {
        return medicines == null ? List.of() : medicines.stream().map(PrescriptionMedicineResponse::from).toList();
    }
}
