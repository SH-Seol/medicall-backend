package com.medicall.domain.doctor;

import com.medicall.domain.department.Department;
import com.medicall.domain.hospital.Hospital;

public record Doctor(
        Long id,
        String name,
        Hospital hospital,
        String introduction,
        String imageUrl,
        Department department,
        String oauthId,
        String provider
) {
    public Doctor(String name, String introduction, Hospital hospital, String imageUrl, Department department, String oauthId, String provider){
        this(null, name, hospital, introduction, imageUrl, department, oauthId, provider);
    }

    /**
     * oauth 용
     * @param name 의사명
     * @param imageUrl 프로필 이미지 url
     * @param oauthId oauthId
     * @param provider 소셜 서비스 종류 ex.카카오, 구글, 네이버
     */
    public Doctor(String name, String imageUrl, String oauthId, String provider){
        this(null, name, null, null, imageUrl, null, oauthId, provider);
    }

    public boolean isProfileComplete(){
        return this.department != null;
    }
}
