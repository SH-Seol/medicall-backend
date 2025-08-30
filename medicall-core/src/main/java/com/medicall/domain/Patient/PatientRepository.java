package com.medicall.domain.Patient;

import java.util.Optional;

public interface PatientRepository {
    Optional<Patient> findById(Long patientId);
}
