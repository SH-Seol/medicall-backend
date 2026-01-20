package com.medicall.domain.appointment;

import com.medicall.domain.address.Address;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;

import java.time.LocalDateTime;

public record Appointment(
        Long id,
        Patient patient,
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        Hospital hospital,
        Doctor doctor,
        AppointmentStatus status
) {
    public Appointment assignDoctor(Doctor doctor) {
        return new Appointment(
                this.id,
                this.patient,
                this.address,
                this.symptom,
                this.reservationTime,
                this.hospital,
                doctor,
                this.status
        );
    }
}
