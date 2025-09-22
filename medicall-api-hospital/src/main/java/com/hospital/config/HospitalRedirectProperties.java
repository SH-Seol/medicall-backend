package com.hospital.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.hospital.redirect")
@Validated
public record HospitalRedirectProperties(
        String setUpUrl,
        String dashboardUrl
) {
}
