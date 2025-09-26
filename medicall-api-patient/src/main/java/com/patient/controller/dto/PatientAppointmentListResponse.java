package com.patient.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.Appointment;

@Schema(description = "환자 예약 응답")
public record PatientAppointmentListResponse(
        @Schema(description = "예약 id", example = "10")
        Long appointmentId,

        @Schema(description = "예약에 등록된 의사 이름", example = "홍길동")
        String doctorName,

        @Schema(description = "예약 병원", example = "메디콜정형외과")
        String hospitalName,

        @Schema(description = "예약 시간", example = "09.26 14:30")
        LocalDateTime reservationTime,

        @Schema(description = "예약 상태", example = "ASSIGNED")
        String status
) {
    /**
     * Appointment 도메인 모델에서 Response로 변환
     */
    public static PatientAppointmentListResponse from(Appointment appointment) {
        return new PatientAppointmentListResponse(
                appointment.id(),
                appointment.doctor().name(),
                appointment.hospital().name(),
                appointment.reservationTime(),
                appointment.status()
        );
    }
}
