package com.doctor.controller.v1.treatment.dto.request;

import com.medicall.domain.treatment.dto.DoctorTreatmentListCriteria;

public record DoctorTreatmentListRequest(
        Long cursorId,
        int size
) {
    public DoctorTreatmentListCriteria toCriteria(Long doctorId, Long patientId) {
        return new DoctorTreatmentListCriteria(doctorId, patientId, cursorId, size);
    }
}
