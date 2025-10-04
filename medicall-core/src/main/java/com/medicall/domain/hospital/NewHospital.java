package com.medicall.domain.hospital;

public record NewHospital(
        String name,
        String imageUrl,
        String oauthId,
        String provider,
        String email
) {
}
