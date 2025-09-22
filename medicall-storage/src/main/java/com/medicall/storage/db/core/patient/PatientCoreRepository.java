package com.medicall.storage.db.core.patient;

import com.medicall.domain.patient.Patient;
import com.medicall.domain.patient.PatientRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PatientCoreRepository implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;

    public PatientCoreRepository(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    public Optional<Patient> findById(Long patientId){
        return patientJpaRepository.findById(patientId).map(PatientEntity::toDomainModel);
    }
}
