package com.medicall.domain.Patient;

import java.util.List;

public record Patient(
        Long id,
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        List<String> chronicDiseases
) {
}
