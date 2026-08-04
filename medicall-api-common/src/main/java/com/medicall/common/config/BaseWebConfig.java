package com.medicall.common.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class BaseWebConfig implements WebMvcConfigurer {

    /**
     * 인증을 HttpOnly 쿠키로 하기 때문에 allowCredentials(true)가 필요하고,
     * 이 경우 오리진에 와일드카드를 쓸 수 없어 allowedOriginPatterns로 지정한다.
     * allowedOrigin은 콤마로 여러 개를 넣을 수 있다.
     */
    protected void configureBasicCors(CorsRegistry registry, String allowedOrigin, long maxAge) {
        registry.addMapping("/**")
                .allowedOriginPatterns(toOriginPatterns(allowedOrigin))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true)
                .maxAge(maxAge);
    }

    private String[] toOriginPatterns(String allowedOrigin) {
        List<String> origins = Arrays.stream(allowedOrigin.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();

        return origins.toArray(String[]::new);
    }
}
