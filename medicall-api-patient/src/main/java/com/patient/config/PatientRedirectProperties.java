package com.patient.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.patient.redirect")
@Validated
public record PatientRedirectProperties(
        // 최초 로그인 시 이동할 약관 동의 화면
        @NotBlank
        String termsUrl,
        // 프로필이 완성된 사용자가 이동할 화면
        @NotBlank
        String dashboardUrl
) {
}
