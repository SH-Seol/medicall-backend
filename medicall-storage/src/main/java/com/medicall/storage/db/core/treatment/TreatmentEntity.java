package com.medicall.storage.db.core.treatment;

import com.medicall.domain.treatment.Treatment;
import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.patient.PatientEntity;
import com.medicall.storage.db.core.prescription.PrescriptionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class TreatmentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @Column(nullable = false)
    private String symptom;

    @Column(nullable = false)
    private String treatment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", unique = true)
    private PrescriptionEntity prescription;

    private String detailTreatment;

    protected TreatmentEntity() {}

    public TreatmentEntity(PatientEntity patient, DoctorEntity doctor, String symptom, String treatment, String detailTreatment) {
        this.patient = patient;
        this.doctor = doctor;
        this.symptom = symptom;
        this.treatment = treatment;
        this.detailTreatment = detailTreatment;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public String getSymptom() {
        return symptom;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getDetailTreatment() {
        return detailTreatment;
    }

    public Treatment toDomainModel(){
        return new Treatment(
                this.id,
                this.patient.toDomainModel(),
                this.doctor.toDomainModel(),
                this.symptom,
                this.treatment,
                this.detailTreatment,
                this.prescription.toDomainModel()
        );
    }
}
