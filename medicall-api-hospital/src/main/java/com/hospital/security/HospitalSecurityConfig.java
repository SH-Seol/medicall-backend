package com.hospital.security;

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
public class HospitalSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CsrfHeaderFilter csrfHeaderFilter;
    private final HospitalOAuthSuccessHandler hospitalOAuthSuccessHandler;

    public HospitalSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 CsrfHeaderFilter csrfHeaderFilter,
                                  HospitalOAuthSuccessHandler hospitalOAuthSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.csrfHeaderFilter = csrfHeaderFilter;
        this.hospitalOAuthSuccessHandler = hospitalOAuthSuccessHandler;
    }

    /**
     * 토큰 재발급·로그아웃 등 auth API 전용 체인.
     * oauth2Login이 걸린 체인에 두면 AuthException(=AuthenticationException) 발생 시
     * 카카오 인가 URL로 302 되므로 분리한다.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain hospitalAuthFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/hospital/auth/**")
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

    @Bean
    @Order(1)
    public SecurityFilterChain hospitalOauthFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/oauth2/**",
                        "/login/**"
                )
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(oauth -> oauth.successHandler(hospitalOAuthSuccessHandler));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain hospitalApiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/v1/hospital/**")
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
                                "/swagger-resources/**",
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
