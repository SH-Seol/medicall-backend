package com.medicall.domain.patient;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class PatientReader {

    private final PatientRepository patientRepository;

    public PatientReader(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient findById(Long patientId){
        return patientRepository.findById(patientId).orElseThrow(() -> new CoreException(CoreErrorType.PATIENT_NOT_FOUND));
    }
}
