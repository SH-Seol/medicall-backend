package com.medicall.domain.patient;

import java.util.Optional;

import com.medicall.domain.patient.dto.PatientProfileUpdate;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository {
    Optional<Patient> findById(Long patientId);
    Patient create(NewPatient newPatient);
    Optional<Patient> findByOAuthInfo(String oauthId, String provider);
    Patient updateProfile(Long patientId, PatientProfileUpdate profileUpdate);
}
