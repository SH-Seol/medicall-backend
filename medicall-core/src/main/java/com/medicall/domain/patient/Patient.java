package com.medicall.domain.patient;

import java.time.LocalDate;
import java.util.List;

public record Patient(
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
    public boolean isProfileComplete(){
        return gender != null && bloodType != null && height != null && weight != null
                && age >= 0;
    }
}
