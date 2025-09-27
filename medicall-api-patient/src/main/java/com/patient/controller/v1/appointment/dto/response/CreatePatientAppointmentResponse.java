package com.patient.controller.v1.appointment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.dto.CreateAppointmentResult;

@Schema(name = "환자 예약 생성 응답")
public record CreatePatientAppointmentResponse(
        @Schema(name = "병원 이름", example = "메디콜정형외과")
        String hospital,
        @Schema(name = "예약 요청 시간", example = "2025-9-27T15:00:00")
        LocalDateTime reservationTime,
        @Schema(name = "의사 이름", example = "홍길동")
        String doctor
) {
    /**
     * 생성된 Appointment 도메인 모델을 응답으로 전환
     */
    public static CreatePatientAppointmentResponse from(CreateAppointmentResult result){
        return new CreatePatientAppointmentResponse(
                result.hospital(),
                result.reservationTime(),
                result.doctor()
        );
    }
}
