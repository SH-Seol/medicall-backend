package com.medicall.domain.invitation;

import java.time.LocalDateTime;

public record NewDoctorInvitation(
        Long hospitalId,
        String code,
        LocalDateTime expiresAt
) {
}
