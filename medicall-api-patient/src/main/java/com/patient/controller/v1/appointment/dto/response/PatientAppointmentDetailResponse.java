package com.patient.controller.v1.appointment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.common.enums.AppointmentStatus;

@Schema(description = "환자 예약 상세 응답")
public record PatientAppointmentDetailResponse(
        @Schema(description = "예약 id")
        Long appointmentId,

        @Schema(description = "배정된 의사 이름 (미배정이면 null)", nullable = true)
        String doctorName,

        @Schema(description = "배정된 의사 id (미배정이면 null)", nullable = true)
        Long doctorId,

        @Schema(description = "예약 병원명")
        String hospitalName,

        @Schema(description = "예약 병원 id")
        Long hospitalId,

        @Schema(description = "환자가 입력한 증상")
        String symptom,

        @Schema(description = "예약 상태")
        AppointmentStatus status,

        @Schema(description = "예약 시간")
        LocalDateTime reservationTime
) {
    public static PatientAppointmentDetailResponse from(Appointment appointment) {
        return new PatientAppointmentDetailResponse(
                appointment.id(),
                appointment.doctor() != null ? appointment.doctor().name() : null,
                appointment.doctor() != null ? appointment.doctor().id() : null,
                appointment.hospital().name(),
                appointment.hospital().id(),
                appointment.symptom(),
                appointment.status(),
                appointment.reservationTime()
        );
    }
}
