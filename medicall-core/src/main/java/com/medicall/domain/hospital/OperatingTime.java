package com.medicall.domain.hospital;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.medicall.domain.common.enums.BusinessStatus;

public record OperatingTime(
        DayOfWeek dayOfWeek,
        boolean isClosed,
        LocalTime openingTime,
        LocalTime closingTime,
        LocalTime breakStartTime,
        LocalTime breakFinishTime
) {
    /**
     * 해당 요일의 특정 시각이 영업/휴게/영업종료 중 무엇인지 판정한다.
     */
    public BusinessStatus statusAt(LocalTime time) {
        if (isClosed || openingTime == null || closingTime == null) {
            return BusinessStatus.CLOSED;
        }
        if (time.isBefore(openingTime) || !time.isBefore(closingTime)) {
            return BusinessStatus.CLOSED;
        }
        if (isInBreak(time)) {
            return BusinessStatus.BREAK;
        }
        return BusinessStatus.OPEN;
    }

    private boolean isInBreak(LocalTime time) {
        return breakStartTime != null && breakFinishTime != null
                && !time.isBefore(breakStartTime) && time.isBefore(breakFinishTime);
    }
}
