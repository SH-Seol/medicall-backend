package com.medicall.domain.patient;

import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientReader patientReader;

    public PatientService(PatientReader patientReader) {
        this.patientReader = patientReader;
    }

    public Patient findById(Long patientId){
        return patientReader.findById(patientId);
    }
}
