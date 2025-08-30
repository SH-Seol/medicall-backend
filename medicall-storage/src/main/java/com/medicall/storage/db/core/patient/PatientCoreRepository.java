package com.medicall.storage.db.core.patient;

import com.medicall.domain.Patient.Patient;
import com.medicall.domain.Patient.PatientRepository;
import java.util.Optional;

public class PatientCoreRepository implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    public PatientCoreRepository(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    public Optional<Patient> findById(Long patientId){
        return patientJpaRepository.findById(patientId).map(PatientEntity::toDomainModel);
    }
}
