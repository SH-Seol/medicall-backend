package com.hospital.controller.appointment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "예약 의사 배정 요청")
public record AssignDoctorRequest(
        @Schema(description = "배정할 의사 id", example = "3")
        @NotNull
        @Positive
        Long doctorId
) {
}
