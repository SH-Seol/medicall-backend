package com.doctor.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.doctor.support.DoctorArgumentResolver;
import com.medicall.common.config.BaseWebConfig;

@Configuration
public class DoctorWebConfig extends BaseWebConfig {

    @Value("${medicall.doctor.cors.allowed-origin}")
    private String corsAllowedOrigin;

    @Value("${medicall.doctor.cors.max-age}")
    private long maxAge;

    private final DoctorArgumentResolver doctorArgumentResolver;

    public DoctorWebConfig(DoctorArgumentResolver doctorArgumentResolver) {
        this.doctorArgumentResolver = doctorArgumentResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configureBasicCors(registry, corsAllowedOrigin, maxAge);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(doctorArgumentResolver);
    }
}
