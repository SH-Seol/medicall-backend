package com.patient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.medicall.common.config.BaseWebConfig;

@Configuration
public class PatientWebConfig extends BaseWebConfig {

    @Value("${medicall.patient.cors.allowed-origin}")
    private String corsAllowedOrigin;

    @Value("${medicall.patient.cors.max-age}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configureBasicCors(registry, corsAllowedOrigin, maxAge);
    }
}
