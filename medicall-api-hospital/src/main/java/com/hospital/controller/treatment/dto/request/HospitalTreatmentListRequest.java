package com.hospital.controller.treatment.dto.request;

import com.medicall.domain.treatment.dto.HospitalTreatmentListCriteria;

public record HospitalTreatmentListRequest(
        Long cursorId,
        int size
) {
    public HospitalTreatmentListCriteria toCriteria(Long hospitalId) {
        return new HospitalTreatmentListCriteria(hospitalId, cursorId, size);
    }
}
