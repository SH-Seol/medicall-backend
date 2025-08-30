package com.medicall.domain.prescription;

import com.medicall.domain.medicine.Medicine;

public record PrescriptionMedicine(
        Medicine medicine,
        double dosage,
        String dosageUnit,
        int quantity,
        String frequency,
        String instruction
) {
}
