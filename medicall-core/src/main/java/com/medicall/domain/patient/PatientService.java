package com.medicall.domain.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.patient.dto.PatientDetailResult;
import com.medicall.domain.patient.dto.PatientProfileUpdate;

@Service
public class PatientService {

    private final PatientReader patientReader;
    private final PatientWriter patientWriter;

    public PatientService(PatientReader patientReader, PatientWriter patientWriter) {
        this.patientReader = patientReader;
        this.patientWriter = patientWriter;
    }

    @Transactional(readOnly = true)
    public PatientDetailResult findById(Long patientId){
        return patientReader.findById(patientId);
    }

    /**
     * 환자 내 정보 수정 (null인 항목은 기존 값 유지)
     */
    @Transactional
    public PatientDetailResult updateMyProfile(Long patientId, PatientProfileUpdate profileUpdate){
        Patient patient = patientWriter.updateProfile(patientId, profileUpdate);

        return PatientDetailResult.from(patient);
    }
}
