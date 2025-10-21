package com.medicall.domain.patient;

import java.util.Optional;

import com.medicall.domain.patient.dto.PatientDetailResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class PatientReader {

    private final PatientRepository patientRepository;

    public PatientReader(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDetailResult findById(Long patientId){
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new CoreException(CoreErrorType.PATIENT_NOT_FOUND));

        return PatientDetailResult.from(patient);
    }

    public Optional<Patient> findByOAuthInfo(String oauthId, String provider){
        return patientRepository.findByOAuthInfo(oauthId, provider);
    }
}
