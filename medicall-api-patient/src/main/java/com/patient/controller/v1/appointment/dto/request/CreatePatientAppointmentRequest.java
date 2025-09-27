package com.patient.controller.v1.appointment.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import org.springdoc.core.annotations.ParameterObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.NewAppointment;

@ParameterObject
@Schema(description = "환자 예약 요청")
public record CreatePatientAppointmentRequest(
        @Parameter(description = "의사 ID", example = "10")
        Long doctorId,
        @Parameter(description = "증상", example = "류마티스 관절염을 앓고 있습니다.")
        String symptom,
        @NotNull
        @Parameter(description = "병원 ID", example = "10")
        Long hospitalId,
        @Parameter(description = "희망 예약 시간", example = "2025-9-27T15:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime appointmentTime,
        @Parameter(description = "환자 주소")
        Address address
) {
        /**
         * 예약 요청으로 변경
         */
        public NewAppointment toNewAppointment() {
                return new NewAppointment(
                        this.symptom,
                        this.doctorId,
                        this.hospitalId,
                        this.appointmentTime,
                        this.address
                );
        }
}
