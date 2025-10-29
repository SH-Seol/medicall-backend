package com.medicall.domain.prescription;

import org.springframework.stereotype.Component;

import com.medicall.domain.prescription.dto.CreatePrescriptionResult;

@Component
public class PrescriptionWriter {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionWriter(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public CreatePrescriptionResult save(NewPrescription newPrescription) {
        Prescription prescription = prescriptionRepository.save(newPrescription);

        return new CreatePrescriptionResult(prescription.id(), prescription.patient().name(), prescription.date());
    }
}
