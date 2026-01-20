package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.AppointmentStatus;

public record AppointmentListResult(
        Long appointmentId,
        String patient,
        String doctor,
        String hospital,
        LocalDateTime reservationTime,
        AppointmentStatus status
) {
    public static AppointmentListResult from(Appointment appointment){
        return new AppointmentListResult(
                appointment.id(),
                appointment.patient().name(),
                appointment.doctor().name(),
                appointment.hospital().name(),
                appointment.reservationTime(),
                appointment.status()
        );
    }
}
