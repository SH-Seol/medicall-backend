package com.medicall.storage.db.domain.patient;

import com.medicall.domain.patient.ContactPerson;
import com.medicall.domain.patient.Patient;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.common.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_patient_oauth",
                        columnNames = {"oauth_provider", "oauth_id"}
                ),
                @UniqueConstraint(
                        name = "uk_patient_email",
                        columnNames = {"email"}
                )
        }
)
public class PatientEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String bloodType;
    private Double height;
    private Double weight;

    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_id")
    private List<AddressEntity> addresses;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientChronicDiseaseEntity> chronicDiseaseEntities = new ArrayList<>();

    private String imageUrl;
    private String email;
    private String oauthId;
    private String oauthProvider;

    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhoneNumber;

    private String guardianName;
    private String guardianRelationship;
    private String guardianPhoneNumber;

    protected PatientEntity() {}

    public PatientEntity(String name, String imageUrl, String email, String oauthId, String oauthProvider) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.email = email;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactRelationship() {
        return emergencyContactRelationship;
    }

    public String getEmergencyContactPhoneNumber() {
        return emergencyContactPhoneNumber;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianRelationship() {
        return guardianRelationship;
    }

    public String getGuardianPhoneNumber() {
        return guardianPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public List<PatientChronicDiseaseEntity> getChronicDiseaseEntities() {
        return chronicDiseaseEntities;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getOauthId() {
        return oauthId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public Patient toDomainModel(){
        return new Patient(this.id,
                this.name,
                this.gender != null ? this.gender.name() : null,
                this.bloodType,
                this.height,
                this.weight,
                this.age,
                this.chronicDiseaseEntities.stream()
                        .map(PatientChronicDiseaseEntity::getDisease)
                        .map(ChronicDiseaseEntity::getName).toList(),
                this.imageUrl,
                this.email,
                this.dateOfBirth,
                new ContactPerson(this.emergencyContactName, this.emergencyContactRelationship, this.emergencyContactPhoneNumber),
                new ContactPerson(this.guardianName, this.guardianRelationship, this.guardianPhoneNumber));
    }

    /**
     * 내 정보 수정 (null인 항목은 기존 값을 유지한다)
     */
    public void updateProfile(String name, Gender gender, String bloodType, Double height, Double weight,
                              LocalDate dateOfBirth, String imageUrl,
                              ContactPerson emergencyContact, ContactPerson guardian) {
        if (name != null) {
            this.name = name;
        }
        if (gender != null) {
            this.gender = gender;
        }
        if (bloodType != null) {
            this.bloodType = bloodType;
        }
        if (height != null) {
            this.height = height;
        }
        if (weight != null) {
            this.weight = weight;
        }
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
            this.age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
        if (emergencyContact != null) {
            this.emergencyContactName = emergencyContact.name();
            this.emergencyContactRelationship = emergencyContact.relationship();
            this.emergencyContactPhoneNumber = emergencyContact.phoneNumber();
        }
        if (guardian != null) {
            this.guardianName = guardian.name();
            this.guardianRelationship = guardian.relationship();
            this.guardianPhoneNumber = guardian.phoneNumber();
        }
    }

    /**
     * 만성 질환 목록 전체 교체
     */
    public void replaceChronicDiseases(List<ChronicDiseaseEntity> diseases) {
        this.chronicDiseaseEntities.clear();
        diseases.forEach(disease -> this.chronicDiseaseEntities.add(new PatientChronicDiseaseEntity(this, disease)));
    }
}
