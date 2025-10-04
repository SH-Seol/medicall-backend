package com.medicall.storage.db.domain.patient;

import com.medicall.domain.patient.NewPatient;
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

    public Patient create(NewPatient newPatient) {
        return patientJpaRepository.save(new PatientEntity(
                newPatient.name(),
                newPatient.imageUrl(),
                newPatient.email(),
                newPatient.oauthId(),
                newPatient.provider()
        )).toDomainModel();
    }

    public Optional<Patient> findByOAuthInfo(String oauthId, String provider){
        return patientJpaRepository.findByOauthIdAndOauthProvider(oauthId, provider).map(PatientEntity::toDomainModel);
    }
}
