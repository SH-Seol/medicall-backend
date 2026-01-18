package com.medicall.common.security;

public record DevLoginResponse(
        Long id,
        String name,
        String accessToken
) {
}
