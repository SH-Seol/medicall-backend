package com.medicall.domain.doctor.dto;

import com.medicall.domain.doctor.Doctor;

public record DoctorResult(
        Long id,
        String name,
        Long hospitalId,
        String hospitalName,
        String introduction,
        String imageUrl,
        Long departmentId,
        String department,
        Long specialtyId,
        String specialty
) {
    public static DoctorResult from(Doctor doctor) {
        return new DoctorResult(
                doctor.id(),
                doctor.name(),
                doctor.hospital() != null ? doctor.hospital().id() : null,
                doctor.hospital() != null ? doctor.hospital().name() : null,
                doctor.introduction(),
                doctor.imageUrl(),
                doctor.department() != null ? doctor.department().id() : null,
                doctor.department() != null ? doctor.department().name() : null,
                doctor.specialty() != null ? doctor.specialty().id() : null,
                doctor.specialty() != null ? doctor.specialty().name() : null
        );
    }
}
