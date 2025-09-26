package com.medicall.storage.db.core.appointment;

import com.medicall.domain.appointment.Appointment;
import com.medicall.storage.db.core.address.AddressEntity;
import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.common.enums.AppointmentStatus;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.hospital.HospitalEntity;
import com.medicall.storage.db.core.patient.PatientEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
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

    public AppointmentEntity(PatientEntity patient, DoctorEntity doctor, String symptom, LocalDateTime reservationTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.symptom = symptom;
        this.reservationTime = reservationTime;
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
                this.patient.getName(),
                this.patientAddress.toDomainModel(),
                this.symptom,
                this.reservationTime,
                this.hospital.toDomainModel(),
                this.doctor.toDomainModel(),
                this.status.toString()
                );
    }

    public void rejectAppointment(){
        if(this.status.equals(AppointmentStatus.REQUESTED)){
            this.status = AppointmentStatus.REJECTED;
        }
        throw new IllegalArgumentException("거절할 수 없는 예약입니다.");
    }

    public void addDoctor(DoctorEntity doctor){
        this.doctor = doctor;
        doctor.getAppointments().add(this);
    }
}
