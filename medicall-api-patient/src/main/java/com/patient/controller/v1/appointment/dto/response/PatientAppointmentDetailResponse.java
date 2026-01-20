package com.patient.controller.v1.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.AppointmentStatus;

public record PatientAppointmentDetailResponse(
        String doctorName,
        Long doctorId,
        String hospitalName,
        Long hospitalId,
        AppointmentStatus status,
        LocalDateTime reservationTime
) {
    public static PatientAppointmentDetailResponse from(Appointment appointment) {
        return new PatientAppointmentDetailResponse(
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
