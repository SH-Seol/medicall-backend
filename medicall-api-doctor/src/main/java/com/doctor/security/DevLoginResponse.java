package com.doctor.security;

public record DevLoginResponse(
        Long doctorId,
        String name,
        String accessToken
) {
}
