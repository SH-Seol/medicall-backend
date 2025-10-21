package com.hospital.controller.patient.dto.response;

import java.util.List;

import com.medicall.domain.patient.dto.PatientDetailResult;

public record HospitalPatientDetailResponse(
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        List<String> chronicDiseases
) {
    public static HospitalPatientDetailResponse from(PatientDetailResult result){
        return new HospitalPatientDetailResponse(
                result.name(),
                result.gender(),
                result.bloodType(),
                result.height(),
                result.weight(),
                result.age(),
                result.chronicDiseases()
        );
    }
}
