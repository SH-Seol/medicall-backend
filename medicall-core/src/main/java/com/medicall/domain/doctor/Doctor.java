package com.medicall.domain.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.medicall.domain.department.Department;
import com.medicall.domain.department.Specialty;
import com.medicall.domain.hospital.Hospital;

public record Doctor(
        Long id,
        String name,
        Hospital hospital,
        String introduction,
        String imageUrl,
        Department department,
        Specialty specialty,
        // 소셜 식별자는 외부로 나가면 안 된다. (응답 DTO가 도메인 객체를 그대로 노출하는 곳이 있다)
        @JsonIgnore String oauthId,
        @JsonIgnore String provider
) {
    public Doctor(String name, String introduction, Hospital hospital, String imageUrl, Department department, String oauthId, String provider){
        this(null, name, hospital, introduction, imageUrl, department, null, oauthId, provider);
    }

    /**
     * oauth 용
     * @param name 의사명
     * @param imageUrl 프로필 이미지 url
     * @param oauthId oauthId
     * @param provider 소셜 서비스 종류 ex.카카오, 구글, 네이버
     */
    public Doctor(String name, String imageUrl, String oauthId, String provider){
        this(null, name, null, null, imageUrl, null, null, oauthId, provider);
    }

    public boolean isProfileComplete(){
        return this.department != null;
    }
}
