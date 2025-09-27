package com.patient.controller.v1.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;

public record PatientAppointmentResponse(
        String doctorName,
        Long doctorId,
        String hospitalName,
        Long hospitalId,
        String status,
        LocalDateTime reservationTime
) {
    public static PatientAppointmentResponse from(Appointment appointment) {
        return new PatientAppointmentResponse(
                appointment.doctor()
                        .name(),
                appointment.doctor()
                        .id(),
                appointment.hospital()
                        .name(),
                appointment.hospital()
                        .id(),
                appointment.status(),
                appointment.reservationTime()
        );
    }
}
