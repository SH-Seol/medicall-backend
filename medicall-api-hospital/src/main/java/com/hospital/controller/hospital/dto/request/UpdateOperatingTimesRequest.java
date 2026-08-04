package com.hospital.controller.hospital.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.medicall.domain.hospital.OperatingTime;

@Schema(description = "병원 주간 업무 시간 수정 요청 (전달한 요일 기준으로 전체 교체)")
public record UpdateOperatingTimesRequest(
        @NotEmpty(message = "업무 시간은 최소 1개 이상이어야 합니다.")
        @Valid
        List<OperatingTimeRequest> operatingTimes
) {
    public List<OperatingTime> toOperatingTimes() {
        return operatingTimes.stream()
                .map(OperatingTimeRequest::toOperatingTime)
                .toList();
    }

    public record OperatingTimeRequest(
            @Schema(description = "요일", example = "MONDAY")
            @NotNull
            DayOfWeek dayOfWeek,

            @Schema(description = "휴진 여부", example = "false")
            boolean isClosed,

            @Schema(description = "진료 시작 시간", example = "09:00")
            @NotNull
            LocalTime openingTime,

            @Schema(description = "진료 종료 시간", example = "18:00")
            @NotNull
            LocalTime closingTime,

            @Schema(description = "휴게 시작 시간", example = "12:30")
            LocalTime breakStartTime,

            @Schema(description = "휴게 종료 시간", example = "13:30")
            LocalTime breakFinishTime
    ) {
        public OperatingTime toOperatingTime() {
            return new OperatingTime(dayOfWeek, isClosed, openingTime, closingTime, breakStartTime, breakFinishTime);
        }
    }
}
