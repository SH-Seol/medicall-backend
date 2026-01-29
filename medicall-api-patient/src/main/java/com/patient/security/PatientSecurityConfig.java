package com.patient.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.medicall.common.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class PatientSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PatientOAuthSuccessHandler patientOAuthSuccessHandler;

    public PatientSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 PatientOAuthSuccessHandler patientOAuthSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.patientOAuthSuccessHandler = patientOAuthSuccessHandler;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain patientOauthFilterChain(HttpSecurity http) throws Exception {
        http.
                securityMatcher(
                        "oauth2/**",
                        "/login/**",
                        "/api/v1/patient/auth/**"
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(oauth -> oauth.successHandler(patientOAuthSuccessHandler));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain patientApiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/patient/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                );

        return http.build();
    }

}
