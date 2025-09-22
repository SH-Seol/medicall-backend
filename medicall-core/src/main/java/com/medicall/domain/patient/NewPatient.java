package com.medicall.domain.patient;

public record NewPatient(
        String name,
        String imageUrl,
        String email,
        String oauthId,
        String provider
) {
}
