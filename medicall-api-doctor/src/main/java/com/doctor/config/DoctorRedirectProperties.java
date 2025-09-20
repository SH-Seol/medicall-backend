package com.doctor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.doctor.redirect")
@Validated
public record DoctorRedirectProperties(
        String profileCompleteUrl,
        String dashboardUrl
) {
}
