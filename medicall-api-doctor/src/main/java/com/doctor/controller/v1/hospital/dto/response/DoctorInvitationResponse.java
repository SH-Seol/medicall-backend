package com.doctor.controller.v1.hospital.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import com.medicall.domain.invitation.DoctorInvitation;

@Schema(description = "병원 초대 정보")
public record DoctorInvitationResponse(
        String code,
        Long hospitalId,
        String hospitalName,
        String status,
        LocalDateTime expiresAt
) {
    public static DoctorInvitationResponse from(DoctorInvitation invitation) {
        return new DoctorInvitationResponse(
                invitation.code(),
                invitation.hospitalId(),
                invitation.hospitalName(),
                invitation.status().name(),
                invitation.expiresAt()
        );
    }
}
