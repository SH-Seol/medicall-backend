package com.doctor.controller.v1.appointment.dto.request;

import com.medicall.domain.appointment.dto.DoctorAppointmentListCriteria;

public record DoctorAppointmentListRequest(
        Long cursorId,
        int size
) {
    public DoctorAppointmentListCriteria toCriteria(Long doctorId) {
        return new DoctorAppointmentListCriteria(doctorId, cursorId, size);
    }
}
