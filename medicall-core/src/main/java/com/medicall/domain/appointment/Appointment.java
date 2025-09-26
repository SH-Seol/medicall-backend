package com.medicall.domain.appointment;

import com.medicall.domain.address.Address;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import java.time.LocalDateTime;

public record Appointment(
        Long id,
        String patientName,
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        Hospital hospital,
        Doctor doctor,
        String status
) {
    public Appointment assignDoctor(Doctor doctor) {
        return new Appointment(
                this.id,
                this.patientName,
                this.address,
                this.symptom,
                this.reservationTime,
                this.hospital,
                doctor,
                this.status
        );
    }
}
