package com.medicall.common.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class BaseWebConfig implements WebMvcConfigurer {

    protected void configureBasicCors(CorsRegistry registry, String allowedOrigin, long maxAge) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(maxAge);
    }
}
