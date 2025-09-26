package com.patient.controller.v1.appointment.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springdoc.core.annotations.ParameterObject;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;

@ParameterObject
@Schema(description = "환자 예약 목록 조회 요청")
public record PatientAppointmentListRequest(
        @Parameter(description = "커서 ID (null인 경우 첫 페이지)", example = "10")
        Long cursorId,
        @Parameter(description = "페이지 크기 (1-100)", example = "25")
        int size
) {

    public PatientAppointmentListCriteria toCriteria(Long patientId) {
        return new PatientAppointmentListCriteria(
                patientId,
                cursorId,
                size
        );
    }
}