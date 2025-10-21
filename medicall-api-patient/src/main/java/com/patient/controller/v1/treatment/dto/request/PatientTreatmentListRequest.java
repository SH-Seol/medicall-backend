package com.patient.controller.v1.treatment.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springdoc.core.annotations.ParameterObject;

import com.medicall.domain.treatment.dto.PatientTreatmentListCriteria;

@ParameterObject
@Schema(description = "환자 진료 목록 조회 요청")
public record PatientTreatmentListRequest(
        @Parameter(description = "커서 ID (null인 경우 첫 페이지)", example = "10")
        Long cursorId,
        @Parameter(description = "페이지 크기 (1-100)", example = "25")
        int size
) {
    public PatientTreatmentListCriteria toCriteria(Long patientId){
        return new PatientTreatmentListCriteria(patientId, cursorId, size);
    }
}
