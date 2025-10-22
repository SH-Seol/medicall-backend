package com.doctor.controller.v1.patient.dto.response;

import java.util.List;

import com.medicall.domain.patient.dto.PatientDetailResult;

public record DoctorPatientDetailResponse(
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        List<String> chronicDiseases
) {
    public static DoctorPatientDetailResponse from(PatientDetailResult result){
        return new DoctorPatientDetailResponse(
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
