package com.medicall.domain.doctor.dto;

/**
 * 의사 내 정보 수정 요청 값
 * null인 값은 수정하지 않는다.
 */
public record DoctorProfileUpdate(
        String name,
        String introduction,
        String imageUrl,
        Long departmentId,
        Long specialtyId
) {
}
