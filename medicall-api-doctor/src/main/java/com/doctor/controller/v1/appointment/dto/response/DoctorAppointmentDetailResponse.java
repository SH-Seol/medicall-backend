package com.doctor.controller.v1.appointment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.common.dto.HospitalSummaryResponse;
import com.medicall.common.dto.PatientSummaryResponse;
import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.common.enums.AppointmentStatus;

@Schema(description = "의사 예약 상세 응답")
public record DoctorAppointmentDetailResponse(
        PatientSummaryResponse patient,
        @Schema(description = "방문 주소")
        Address address,
        String symptom,
        LocalDateTime reservationTime,
        HospitalSummaryResponse hospital,
        AppointmentStatus status
) {
    public static DoctorAppointmentDetailResponse from(AppointmentDetailResult result){
        return new DoctorAppointmentDetailResponse(
                PatientSummaryResponse.from(result.patient()),
                result.address(),
                result.symptom(),
                result.reservationTime(),
                HospitalSummaryResponse.from(result.hospital()),
                result.status()
        );
    }
}
