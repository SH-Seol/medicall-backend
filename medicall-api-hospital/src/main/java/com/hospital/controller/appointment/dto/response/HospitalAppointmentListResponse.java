package com.hospital.controller.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.dto.AppointmentListResult;

public record HospitalAppointmentListResponse(
        Long id,
        String patient,
        LocalDateTime reservationTime,
        String doctor,
        String status
) {
    public static HospitalAppointmentListResponse from(AppointmentListResult result){
        return new HospitalAppointmentListResponse(
                result.appointmentId(),
                result.patient(),
                result.reservationTime(),
                result.doctor(),
                result.status()
        );
    }
}
