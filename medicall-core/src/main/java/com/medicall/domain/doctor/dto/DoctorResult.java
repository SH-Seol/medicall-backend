package com.medicall.domain.doctor.dto;

import com.medicall.domain.doctor.Doctor;

public record DoctorResult(
        String name,
        String hospitalName,
        String introduction,
        String imageUrl,
        String department
) {
    public static DoctorResult from(Doctor doctor) {
        return new DoctorResult(
                doctor.name(),
                doctor.hospital().name(),
                doctor.introduction(),
                doctor.imageUrl(),
                doctor.department().name()
        );
    }
}
