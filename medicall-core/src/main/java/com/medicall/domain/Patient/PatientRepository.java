package com.medicall.domain.Patient;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository {
    Optional<Patient> findById(Long patientId);
}
