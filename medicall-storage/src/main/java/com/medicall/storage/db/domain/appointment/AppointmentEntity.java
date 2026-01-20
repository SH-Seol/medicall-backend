package com.medicall.storage.db.domain.appointment;

import com.medicall.domain.appointment.Appointment;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.patient.PatientEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class AppointmentEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @Column(nullable = false)
    private String symptom;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private AddressEntity patientAddress;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus status;

    protected AppointmentEntity() {}

    public AppointmentEntity(PatientEntity patient, DoctorEntity doctor,
                             HospitalEntity hospital, String symptom,
                             AddressEntity address, LocalDateTime reservationTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.hospital = hospital;
        this.symptom = symptom;
        this.patientAddress = address;
        this.reservationTime = reservationTime;
        this.status = AppointmentStatus.REQUESTED;
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

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public AddressEntity getPatientAddress() {
        return patientAddress;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public Appointment toDomainModel(){
        return new Appointment(
                this.id,
                this.patient.toDomainModel(),
                this.patientAddress.toDomainModel(),
                this.symptom,
                this.reservationTime,
                this.hospital.toDomainModel(),
                this.doctor.toDomainModel(),
                this.status
                );
    }

    public void rejectAppointment(){
        this.status = AppointmentStatus.REJECTED;
    }

    public void addDoctor(DoctorEntity doctor){
        this.doctor = doctor;
        doctor.getAppointments().add(this);
    }

    public void acceptAppointment(){
        this.status = AppointmentStatus.ASSIGNED;
    }
}
