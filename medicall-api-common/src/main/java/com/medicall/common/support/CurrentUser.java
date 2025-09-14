package com.medicall.common.support;

public record CurrentUser(
        Long userId,
        String email,
        String name,
        String serviceType, // doctor, patient, hospital
        String provider // kakao, naver
) {
    public boolean isDoctor(){
        return serviceType.equals("Doctor");
    }

    public boolean isPatient(){
        return serviceType.equals("Patient");
    }

    public boolean isHospital(){
        return serviceType.equals("Hospital");
    }
}
