package com.hospital.controller.appointment.dto.request;

import com.medicall.domain.appointment.dto.HospitalAppointmentListCriteria;

public record HospitalAppointmentListRequest(
        Long cursorId,
        int size
) {
    public HospitalAppointmentListCriteria toCriteria(Long hospitalId){
        return new HospitalAppointmentListCriteria(hospitalId, cursorId, size);
    }
}
