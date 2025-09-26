package com.medicall.domain.appointment.dto;

import com.medicall.support.CorePageUtils;

public record PatientAppointmentListCriteria(
        Long patientId,
        Long cursorId,
        int size
) {

    public boolean hasCursor(){
        return CorePageUtils.hasCursor(cursorId);
    }
}
