package com.patient.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.medicall.common.config.BaseWebConfig;
import com.patient.support.PatientArgumentResolver;

@Configuration
public class PatientWebConfig extends BaseWebConfig {

    @Value("${medicall.patient.cors.allowed-origin}")
    private String corsAllowedOrigin;

    @Value("${medicall.patient.cors.max-age}")
    private long maxAge;

    private final PatientArgumentResolver patientArgumentResolver;

    public PatientWebConfig(PatientArgumentResolver patientArgumentResolver) {
        this.patientArgumentResolver = patientArgumentResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configureBasicCors(registry, corsAllowedOrigin, maxAge);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(patientArgumentResolver);
    }
}
