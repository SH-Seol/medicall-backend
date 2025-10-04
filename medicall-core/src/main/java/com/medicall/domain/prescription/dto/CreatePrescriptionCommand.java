package com.medicall.domain.prescription.dto;

import com.medicall.domain.prescription.PrescriptionMedicine;
import java.util.List;

public record CreatePrescriptionCommand(
        Long treatmentId,
        List<PrescriptionMedicine> medicines
) {
}
