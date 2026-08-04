package com.medicall.common.security;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
