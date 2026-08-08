package com.patient.controller.v1.doctor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.appointment.dto.AvailableSlotResult;

@Schema(description = "예약 가능 슬롯")
public record AvailableSlotResponse(
        @Schema(description = "슬롯 시작 시각 (1시간 단위)", example = "2026-08-10T14:00:00")
        LocalDateTime startTime,

        @Schema(description = "예약 가능 여부")
        boolean available,

        @Schema(description = "불가능한 이유 (가능하면 null)", example = "이미 예약됨", nullable = true)
        String unavailableReason
) {
    public static AvailableSlotResponse from(AvailableSlotResult result) {
        return new AvailableSlotResponse(
                result.startTime(),
                result.available(),
                result.unavailableReason()
        );
    }
}
