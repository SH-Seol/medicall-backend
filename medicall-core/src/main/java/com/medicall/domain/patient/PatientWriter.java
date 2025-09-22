package com.medicall.domain.patient;

import org.springframework.stereotype.Component;

@Component
public class PatientWriter {

    private final PatientRepository patientRepository;

    public PatientWriter(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(NewPatient newPatient) {
        return patientRepository.create(newPatient);
    }
}
