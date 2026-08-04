package com.hospital.controller.doctor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.invitation.DoctorInvitation;

@Schema(description = "의사 초대 정보")
public record HospitalInvitationResponse(
        String code,
        String status,
        LocalDateTime expiresAt,
        Long acceptedDoctorId
) {
    public static HospitalInvitationResponse from(DoctorInvitation invitation) {
        return new HospitalInvitationResponse(
                invitation.code(),
                invitation.status().name(),
                invitation.expiresAt(),
                invitation.acceptedDoctorId()
        );
    }
}
