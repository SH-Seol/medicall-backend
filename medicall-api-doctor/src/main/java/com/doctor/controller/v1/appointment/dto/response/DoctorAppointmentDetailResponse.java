package com.doctor.controller.v1.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;

public record DoctorAppointmentDetailResponse(
        Patient patient,
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        Hospital hospital,
        AppointmentStatus status
) {
    public static DoctorAppointmentDetailResponse from(AppointmentDetailResult result){
        return new DoctorAppointmentDetailResponse(
                result.patient(),
                result.address(),
                result.symptom(),
                result.reservationTime(),
                result.hospital(),
                result.status()
        );
    }
}
