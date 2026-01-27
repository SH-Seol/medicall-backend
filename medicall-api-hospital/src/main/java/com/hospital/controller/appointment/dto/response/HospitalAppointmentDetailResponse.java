package com.hospital.controller.appointment.dto.response;

import java.time.LocalDateTime;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.patient.Patient;

public record HospitalAppointmentDetailResponse(
        Patient patient,
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        Doctor doctor,
        AppointmentStatus status
) {
    public static HospitalAppointmentDetailResponse from(AppointmentDetailResult result) {
        return new HospitalAppointmentDetailResponse(
                result.patient(),
                result.address(),
                result.symptom(),
                result.reservationTime(),
                result.doctor(),
                result.status()
        );
    }
}
