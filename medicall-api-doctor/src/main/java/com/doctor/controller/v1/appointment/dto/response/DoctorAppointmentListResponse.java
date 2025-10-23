package com.doctor.controller.v1.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.dto.AppointmentListResult;

public record DoctorAppointmentListResponse(
        Long appointmentId,
        String patient,
        String hospital,
        LocalDateTime reservationTime
) {
    public static DoctorAppointmentListResponse from(AppointmentListResult result) {
        return new DoctorAppointmentListResponse(
                result.appointmentId(),
                result.patient(),
                result.hospital(),
                result.reservationTime()
        );
    }
}
