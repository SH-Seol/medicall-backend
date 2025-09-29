package com.patient.controller.v1.doctor.dto.response;

import com.medicall.domain.doctor.dto.DoctorResult;

public record PatientDoctorResponse(
        String doctor,
        String hospital,
        String department,
        String introduction,
        String imageUrl
) {
    public static PatientDoctorResponse from(DoctorResult result) {
        return new PatientDoctorResponse(
                result.name(),
                result.hospitalName(),
                result.department(),
                result.introduction(),
                result.imageUrl()
        );
    }
}
