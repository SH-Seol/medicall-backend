package com.medicall.domain.invitation;

import java.time.LocalDateTime;

public record DoctorInvitation(
        Long id,
        String code,
        Long hospitalId,
        String hospitalName,
        InvitationStatus status,
        LocalDateTime expiresAt,
        Long acceptedDoctorId
) {
    public boolean isExpired(LocalDateTime now) {
        return expiresAt.isBefore(now);
    }

    public boolean isPending() {
        return status == InvitationStatus.PENDING;
    }
}
