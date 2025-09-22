package com.patient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("medicall.patient.redirect")
@Validated
public record PatientRedirectProperties(
        String termsUrl,
        String dashboardUrl
) {
}
