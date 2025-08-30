package com.medicall.domain.hospital;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OperatingTime(
        DayOfWeek dayOfWeek,
        boolean isClosed,
        LocalTime openingTime,
        LocalTime closingTime,
        LocalTime breakStartTime,
        LocalTime breakFinishTime
) {
}
