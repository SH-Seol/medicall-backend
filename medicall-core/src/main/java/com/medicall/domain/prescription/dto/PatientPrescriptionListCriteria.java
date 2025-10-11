package com.medicall.domain.prescription.dto;

import com.medicall.support.CorePageUtils;

public record PatientPrescriptionListCriteria(
        Long patientId,
        Long cursorId,
        int size
) {
    public boolean hasCursor(){
        return CorePageUtils.hasCursor(cursorId);
    }
}
