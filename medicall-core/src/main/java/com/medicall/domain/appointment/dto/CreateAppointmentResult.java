package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;

public record CreateAppointmentResult(
        Long appointmentId,
        String hospital,
        String doctor,
        LocalDateTime reservationTime
) {
    public static CreateAppointmentResult from(Appointment appointment){
        return new CreateAppointmentResult(
                appointment.id(),
                appointment.hospital().name(),
                // 의사를 지정하지 않고 병원에만 요청할 수 있다.
                appointment.doctor() != null ? appointment.doctor().name() : null,
                appointment.reservationTime()
        );
    }
}
