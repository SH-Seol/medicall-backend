package com.medicall.domain.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.patient.dto.PatientDetailResult;

@Service
public class PatientService {

    private final PatientReader patientReader;

    public PatientService(PatientReader patientReader) {
        this.patientReader = patientReader;
    }

    @Transactional(readOnly = true)
    public PatientDetailResult findById(Long patientId){
        return patientReader.findById(patientId);
    }
}
