package com.hospital.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.hospital.support.HospitalArgumentResolver;
import com.medicall.common.config.BaseWebConfig;

@Configuration
public class HospitalWebConfig extends BaseWebConfig {

    @Value("${medicall.hospital.cors.allowed-origin}")
    private String corsAllowedOrigin;

    @Value("${medicall.hospital.cors.max-age}")
    private long maxAge;

    private final HospitalArgumentResolver hospitalArgumentResolver;

    public HospitalWebConfig(HospitalArgumentResolver hospitalArgumentResolver) {
        this.hospitalArgumentResolver = hospitalArgumentResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        configureBasicCors(registry, corsAllowedOrigin, maxAge);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(hospitalArgumentResolver);
    }
}
