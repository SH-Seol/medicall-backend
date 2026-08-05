package com.hospital.controller.appointment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.common.dto.DoctorSummaryResponse;
import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.common.enums.AppointmentStatus;

@Schema(description = "병원 예약 상세 응답")
public record HospitalAppointmentDetailResponse(
        PatientSummaryResponse patient,
        @Schema(description = "방문 주소")
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        @Schema(description = "배정된 의사 (미배정이면 null)", nullable = true)
        DoctorSummaryResponse doctor,
        AppointmentStatus status
) {
    public static HospitalAppointmentDetailResponse from(AppointmentDetailResult result) {
        return new HospitalAppointmentDetailResponse(
                PatientSummaryResponse.from(result.patient()),
                result.address(),
                result.symptom(),
                result.reservationTime(),
                DoctorSummaryResponse.from(result.doctor()),
                result.status()
        );
    }
}
