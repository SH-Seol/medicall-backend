package com.medicall.domain.appointment.dto;

public record HospitalAppointmentListCriteria(
        Long hospitalId,
        Long cursorId,
        int size
) {
}
