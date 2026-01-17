package com.medicall.storage.db.domain.patient;

import com.medicall.storage.db.domain.common.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_chronic_diseases")
public class PatientChronicDiseaseEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", nullable = false)
    private ChronicDiseaseEntity disease;

    protected PatientChronicDiseaseEntity(){}

    public PatientEntity getPatient() {
        return patient;
    }

    public ChronicDiseaseEntity getDisease() {
        return disease;
    }

    public PatientChronicDiseaseEntity(PatientEntity patient, ChronicDiseaseEntity disease){
        this.patient = patient;
        this.disease = disease;
    }
}
