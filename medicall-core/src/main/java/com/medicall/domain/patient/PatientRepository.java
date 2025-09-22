package com.medicall.domain.patient;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository {
    Optional<Patient> findById(Long patientId);
}
