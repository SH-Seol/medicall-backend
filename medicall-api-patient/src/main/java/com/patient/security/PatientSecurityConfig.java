package com.patient.security;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.medicall.common.security.CsrfHeaderFilter;
import com.medicall.common.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class PatientSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CsrfHeaderFilter csrfHeaderFilter;
    private final PatientOAuthSuccessHandler patientOAuthSuccessHandler;

    public PatientSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 CsrfHeaderFilter csrfHeaderFilter,
                                 PatientOAuthSuccessHandler patientOAuthSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.csrfHeaderFilter = csrfHeaderFilter;
        this.patientOAuthSuccessHandler = patientOAuthSuccessHandler;
    }

    /**
     * 토큰 재발급·로그아웃 등 auth API 전용 체인.
     * oauth2Login이 걸린 체인에 두면 AuthException(=AuthenticationException) 발생 시
     * 카카오 인가 URL로 302 되므로 분리한다.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain patientAuthFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/patient/auth/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(csrfHeaderFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                );

        return http.build();
    }

    /**
     * 약국이 처방전 QR을 스캔하는 경로. 약국은 로그인 주체가 아니므로 QR 토큰 자체가 인증 수단이다.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain prescriptionQrFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/prescriptions/qr/**")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain patientOauthFilterChain(HttpSecurity http) throws Exception {
        http.
                securityMatcher(
                        "oauth2/**",
                        "/login/**"
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(oauth -> oauth.successHandler(patientOAuthSuccessHandler));

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain patientApiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/patient/**", "/api/v1/chats/**")
                .cors(Customizer.withDefaults())
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
                .addFilterBefore(csrfHeaderFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(
                        jwtAuthenticationFilter,
                        CsrfHeaderFilter.class
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                );

        return http.build();
    }

}
