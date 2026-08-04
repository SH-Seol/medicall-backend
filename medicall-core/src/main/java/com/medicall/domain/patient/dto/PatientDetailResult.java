package com.medicall.domain.patient.dto;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.patient.ContactPerson;
import com.medicall.domain.patient.Patient;

public record PatientDetailResult(
        Long id,
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        List<String> chronicDiseases,
        String imageUrl,
        String email,
        LocalDate dateOfBirth,
        ContactPerson emergencyContact,
        ContactPerson guardian
) {
    public static PatientDetailResult from(Patient patient) {
        return new PatientDetailResult(
                patient.id(),
                patient.name(),
                patient.gender(),
                patient.bloodType(),
                patient.height(),
                patient.weight(),
                patient.age(),
                patient.chronicDiseases(),
                patient.imageUrl(),
                patient.email(),
                patient.dateOfBirth(),
                patient.emergencyContact(),
                patient.guardian()
        );
    }
}
