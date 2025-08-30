package com.medicall.domain.prescription;

import java.time.LocalDate;
import java.util.List;

public record NewPrescription(
        Long patientId,
        List<PrescriptionMedicine> medicines,
        Long doctorId,
        Long treatmentId,
        LocalDate date
) {
}
