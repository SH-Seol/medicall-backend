package com.medicall.domain.patient.dto;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.patient.ContactPerson;

/**
 * 환자 내 정보 수정 요청 값
 * null인 값은 수정하지 않는다. (chronicDiseases는 null이 아니면 전체 교체)
 */
public record PatientProfileUpdate(
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        LocalDate dateOfBirth,
        String imageUrl,
        List<String> chronicDiseases,
        ContactPerson emergencyContact,
        ContactPerson guardian
) {
}
