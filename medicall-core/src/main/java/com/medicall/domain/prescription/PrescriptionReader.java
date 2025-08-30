package com.medicall.domain.prescription;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionReader {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionReader(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Prescription> getAllPrescriptionsByPatientIdAndDoctorId(Long patientId, Long doctorId) {
        return prescriptionRepository.getPrescriptionByPatientIdAndDoctorId(patientId, doctorId);
    }

    public Prescription getPrescriptionById(Long prescriptionId) {

        return prescriptionRepository.getPrescriptionById(prescriptionId)
                .orElseThrow(() -> new CoreException(CoreErrorType.PRESCRIPTION_NOT_FOUND));
    }
}
