package com.hospital.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.hospital.redirect")
@Validated
public record HospitalRedirectProperties(
        // 주소·진료과·업무시간 미등록 병원이 이동할 온보딩 화면
        @NotBlank
        String setUpUrl,
        // 온보딩을 마친 병원이 이동할 화면
        @NotBlank
        String dashboardUrl
) {
}
