package com.medicall.domain.hospital.dto;

/**
 * 병원 내 정보 수정 요청 값
 * null인 값은 수정하지 않는다.
 */
public record HospitalProfileUpdate(
        String name,
        String telephoneNumber,
        String imageUrl
) {
}
