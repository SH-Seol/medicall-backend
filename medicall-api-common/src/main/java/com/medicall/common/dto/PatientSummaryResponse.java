package com.medicall.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.patient.Patient;

/**
 * 의사·병원 화면에 필요한 최소한의 환자 정보.
 * 도메인 객체를 그대로 노출하면 이메일·연락처 등 불필요한 정보까지 나간다.
 */
@Schema(description = "환자 요약 정보")
public record PatientSummaryResponse(
        Long patientId,
        String name,
        String gender,
        int age,
        String imageUrl
) {
    public static PatientSummaryResponse from(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new PatientSummaryResponse(
                patient.id(),
                patient.name(),
                patient.gender(),
                patient.age(),
                patient.imageUrl()
        );
    }
}
