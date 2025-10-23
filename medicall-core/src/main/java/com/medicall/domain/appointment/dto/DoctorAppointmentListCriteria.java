package com.medicall.domain.appointment.dto;

import com.medicall.support.CorePageUtils;

public record DoctorAppointmentListCriteria(
        Long doctorId,
        Long cursorId,
        int size
) {
    public boolean hasCursor(){
        return CorePageUtils.hasCursor(cursorId);
    }
}
