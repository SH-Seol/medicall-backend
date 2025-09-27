package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;

public record CreateAppointmentResult(
        String hospital,
        String doctor,
        LocalDateTime reservationTime
) {
    public static CreateAppointmentResult from(Appointment appointment){
        return new CreateAppointmentResult(
                appointment.hospital().name(),
                appointment.doctor().name(),
                appointment.reservationTime()
        );
    }
}
