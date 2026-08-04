package com.doctor.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.doctor.redirect")
@Validated
public record DoctorRedirectProperties(
        // 진료과 미등록 의사가 이동할 온보딩 화면
        @NotBlank
        String profileCompleteUrl,
        // 프로필이 완성된 의사가 이동할 화면
        @NotBlank
        String dashboardUrl
) {
}
