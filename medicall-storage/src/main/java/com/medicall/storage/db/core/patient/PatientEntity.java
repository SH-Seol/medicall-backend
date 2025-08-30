package com.medicall.storage.db.core.patient;

import com.medicall.domain.Patient.Patient;
import com.medicall.storage.db.core.address.AddressEntity;
import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.common.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PatientEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String bloodType;
    private Double height;
    private Double weight;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addresses;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientChronicDiseaseEntity> chronicDiseaseEntities = new ArrayList<>();

    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhoneNumber;

    private String guardianName;
    private String guardianRelationship;
    private String guardianPhoneNumber;

    protected PatientEntity() {}

    public PatientEntity(String name, int age, Gender gender, LocalDate dateOfBirth) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
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

    public Patient toDomainModel(){
        return new Patient(this.id,
                this.name,
                this.gender.toString(),
                this.bloodType,
                this.height,
                this.weight,
                this.age,
                this.chronicDiseaseEntities.stream()
                        .map(PatientChronicDiseaseEntity::getDisease)
                        .map(ChronicDiseaseEntity::getName).toList());
    }
}
