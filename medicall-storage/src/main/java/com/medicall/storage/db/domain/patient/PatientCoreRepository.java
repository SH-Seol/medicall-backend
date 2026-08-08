package com.medicall.storage.db.domain.patient;

import com.medicall.domain.address.Address;
import com.medicall.domain.patient.NewPatient;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.patient.PatientRepository;
import com.medicall.domain.patient.dto.PatientProfileUpdate;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.common.enums.Gender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
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

    @Transactional
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

    public List<Address> findAddresses(Long patientId) {
        return findPatient(patientId).getAddresses().stream()
                .map(AddressEntity::toDomainModel)
                .toList();
    }

    @Transactional
    public Address addAddress(Long patientId, Address address) {
        PatientEntity patientEntity = findPatient(patientId);
        AddressEntity addressEntity = toEntity(address);

        patientEntity.addAddress(addressEntity);
        patientJpaRepository.flush();

        return addressEntity.toDomainModel();
    }

    @Transactional
    public Address updateAddress(Long patientId, Long addressId, Address address) {
        AddressEntity addressEntity = findAddress(patientId, addressId);

        addressEntity.update(
                address.zoneCode(),
                address.roadAddress(),
                address.jibunAddress(),
                address.detailAddress(),
                address.buildingName(),
                address.longitude(),
                address.latitude()
        );

        return addressEntity.toDomainModel();
    }

    @Transactional
    public void deleteAddress(Long patientId, Long addressId) {
        PatientEntity patientEntity = findPatient(patientId);
        AddressEntity addressEntity = findAddressIn(patientEntity, addressId);

        patientEntity.removeAddress(addressEntity);
    }

    @Transactional
    public void changeDefaultAddress(Long patientId, Long addressId) {
        PatientEntity patientEntity = findPatient(patientId);
        AddressEntity addressEntity = findAddressIn(patientEntity, addressId);

        patientEntity.changeDefaultAddress(addressEntity);
    }

    private PatientEntity findPatient(Long patientId) {
        return patientJpaRepository.findById(patientId)
                .orElseThrow(() -> new CoreException(CoreErrorType.PATIENT_NOT_FOUND));
    }

    /**
     * 본인 주소만 다룰 수 있도록 환자를 통해 조회한다.
     */
    private AddressEntity findAddress(Long patientId, Long addressId) {
        return findAddressIn(findPatient(patientId), addressId);
    }

    private AddressEntity findAddressIn(PatientEntity patientEntity, Long addressId) {
        return patientEntity.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorType.ADDRESS_NOT_FOUND));
    }

    private AddressEntity toEntity(Address address) {
        return new AddressEntity(
                address.zoneCode(),
                address.roadAddress(),
                address.jibunAddress(),
                address.detailAddress(),
                address.buildingName(),
                address.longitude(),
                address.latitude()
        );
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
