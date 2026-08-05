package com.medicall.domain.prescription;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

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

    /**
     * 조제 완료 처리. 이미 조제된 처방전이면 실패한다.
     */
    public void dispense(Long prescriptionId) {
        if(!prescriptionRepository.dispense(prescriptionId)){
            throw new CoreException(CoreErrorType.PRESCRIPTION_ALREADY_DISPENSED);
        }
    }
}
