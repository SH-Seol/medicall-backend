package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;

public record AppointmentDetailResult(
        Patient patient,
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        Hospital hospital,
        Doctor doctor,
        AppointmentStatus status
) {
    public static AppointmentDetailResult from(Appointment appointment) {
        return new AppointmentDetailResult(
                appointment.patient(),
                appointment.address(),
                appointment.symptom(),
                appointment.reservationTime(),
                appointment.hospital(),
                appointment.doctor(),
                appointment.status()
        );
    }
}
