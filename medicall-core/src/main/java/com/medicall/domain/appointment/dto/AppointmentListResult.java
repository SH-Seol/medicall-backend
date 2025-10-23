package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;

public record AppointmentListResult(
        Long appointmentId,
        String patient,
        String doctor,
        String hospital,
        LocalDateTime reservationTime
) {
    public static AppointmentListResult from(Appointment appointment){
        return new AppointmentListResult(
                appointment.id(),
                appointment.patient().name(),
                appointment.doctor().name(),
                appointment.hospital().name(),
                appointment.reservationTime()
        );
    }
}
