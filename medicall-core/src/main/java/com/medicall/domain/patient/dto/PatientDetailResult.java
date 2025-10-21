package com.medicall.domain.patient.dto;

import java.util.List;

import com.medicall.domain.patient.Patient;

public record PatientDetailResult(
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        List<String> chronicDiseases
) {
    public static PatientDetailResult from(Patient patient) {
        return new PatientDetailResult(
                patient.name(),
                patient.gender(),
                patient.bloodType(),
                patient.height(),
                patient.weight(),
                patient.age(),
                patient.chronicDiseases()
        );
    }
}
