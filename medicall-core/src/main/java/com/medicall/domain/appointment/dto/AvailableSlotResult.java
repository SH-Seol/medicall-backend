package com.medicall.domain.appointment.dto;

import java.time.LocalDateTime;

/**
 * 예약 가능한 1시간 슬롯
 */
public record AvailableSlotResult(
        LocalDateTime startTime,
        boolean available,
        String unavailableReason
) {
    public static AvailableSlotResult available(LocalDateTime startTime) {
        return new AvailableSlotResult(startTime, true, null);
    }

    public static AvailableSlotResult unavailable(LocalDateTime startTime, String reason) {
        return new AvailableSlotResult(startTime, false, reason);
    }
}
