package com.medicall.domain.prescription;

import org.springframework.stereotype.Component;

@Component
public class PrescriptionWriter {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionWriter(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Long save(NewPrescription newPrescription) {
        return prescriptionRepository.save(newPrescription);
    }
}
