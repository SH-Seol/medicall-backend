package com.medicall.storage.db.domain.prescription;

import com.medicall.domain.prescription.Prescription;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.patient.PatientEntity;
import com.medicall.storage.db.domain.treatment.TreatmentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class PrescriptionEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionMedicineEntity> PrescriptionMedicineList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "treatment_id", unique = true)
    private TreatmentEntity treatment;

    @Column(nullable = false)
    private LocalDate prescriptionDate;

    protected PrescriptionEntity() {}

    public PrescriptionEntity(PatientEntity patient,
                              DoctorEntity doctor,
                              HospitalEntity hospital,
                              List<PrescriptionMedicineEntity> prescriptionMedicineList,
                              TreatmentEntity treatment,
                              LocalDate prescriptionDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.hospital = hospital;
        this.PrescriptionMedicineList = prescriptionMedicineList;
        this.treatment = treatment;
        this.prescriptionDate = prescriptionDate;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public LocalDate getPrescriptionDate() {
        return prescriptionDate;
    }

    public TreatmentEntity getTreatment() {
        return treatment;
    }

    public List<PrescriptionMedicineEntity> getPrescriptionMedicineList() {
        return PrescriptionMedicineList;
    }

    public Prescription toDomainModel(){
        return new Prescription(
                this.id,
                this.patient.toDomainModel(),
                this.PrescriptionMedicineList.stream().map(PrescriptionMedicineEntity::toDomainModel).toList(),
                this.hospital.toDomainModel(),
                this.doctor.toDomainModel(),
                this.treatment.toDomainModel(),
                this.prescriptionDate
        );
    }

    public void addPrescriptionMedicine(PrescriptionMedicineEntity prescriptionMedicineEntity) {
        this.PrescriptionMedicineList.add(prescriptionMedicineEntity);
        prescriptionMedicineEntity.addPrescription(this);
    }
}
