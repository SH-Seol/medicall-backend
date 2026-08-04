package com.medicall.domain.patient;

import org.springframework.stereotype.Component;

import com.medicall.domain.patient.dto.PatientProfileUpdate;

@Component
public class PatientWriter {

    private final PatientRepository patientRepository;

    public PatientWriter(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(NewPatient newPatient) {
        return patientRepository.create(newPatient);
    }

    public Patient updateProfile(Long patientId, PatientProfileUpdate profileUpdate) {
        return patientRepository.updateProfile(patientId, profileUpdate);
    }
}
