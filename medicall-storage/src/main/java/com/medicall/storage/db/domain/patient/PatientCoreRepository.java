package com.medicall.storage.db.domain.patient;

import com.medicall.domain.patient.NewPatient;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.patient.PatientRepository;
import com.medicall.domain.patient.dto.PatientProfileUpdate;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.domain.common.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

@Repository
public class PatientCoreRepository implements PatientRepository {

    private final PatientJpaRepository patientJpaRepository;
    private final ChronicDiseaseJpaRepository chronicDiseaseJpaRepository;

    public PatientCoreRepository(PatientJpaRepository patientJpaRepository,
                                 ChronicDiseaseJpaRepository chronicDiseaseJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
        this.chronicDiseaseJpaRepository = chronicDiseaseJpaRepository;
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

    public Patient updateProfile(Long patientId, PatientProfileUpdate profileUpdate) {
        PatientEntity patientEntity = patientJpaRepository.findById(patientId)
                .orElseThrow(() -> new CoreException(CoreErrorType.PATIENT_NOT_FOUND));

        patientEntity.updateProfile(
                profileUpdate.name(),
                toGender(profileUpdate.gender()),
                profileUpdate.bloodType(),
                profileUpdate.height(),
                profileUpdate.weight(),
                profileUpdate.dateOfBirth(),
                profileUpdate.imageUrl(),
                profileUpdate.emergencyContact(),
                profileUpdate.guardian()
        );

        if (profileUpdate.chronicDiseases() != null) {
            patientEntity.replaceChronicDiseases(findOrCreateDiseases(profileUpdate.chronicDiseases()));
        }

        return patientEntity.toDomainModel();
    }

    private Gender toGender(String gender) {
        if (gender == null) {
            return null;
        }
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CoreException(CoreErrorType.PATIENT_INVALID_GENDER);
        }
    }

    /**
     * 만성 질환명으로 조회하고, 등록되지 않은 질환은 새로 생성한다.
     */
    private List<ChronicDiseaseEntity> findOrCreateDiseases(List<String> diseaseNames) {
        List<String> distinctNames = diseaseNames.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .distinct()
                .toList();

        if (distinctNames.isEmpty()) {
            return List.of();
        }

        Map<String, ChronicDiseaseEntity> existing = chronicDiseaseJpaRepository.findAllByNameIn(distinctNames)
                .stream()
                .collect(Collectors.toMap(ChronicDiseaseEntity::getName, Function.identity(), (before, after) -> before));

        List<ChronicDiseaseEntity> diseases = new ArrayList<>();
        for (String name : distinctNames) {
            ChronicDiseaseEntity disease = existing.get(name);
            diseases.add(disease != null ? disease : chronicDiseaseJpaRepository.save(new ChronicDiseaseEntity(name)));
        }

        return diseases;
    }
}
