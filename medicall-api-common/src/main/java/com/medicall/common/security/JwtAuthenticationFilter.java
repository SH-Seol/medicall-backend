package com.medicall.common.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/health",
            "/actuator/health",
            "auth/login",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            if(shouldSkipFilter(request)){
                filterChain.doFilter(request, response);
                return;
            }

            String token = extractTokenFromRequest(request);

            if(StringUtils.hasText(token)){
                authenticateWithToken(token);
            }

            filterChain.doFilter(request, response);

        }catch (AuthException e){
            log.debug("Authentication 실패: {}", e.getMessage());
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void authenticateWithToken(String token){
        if(!jwtTokenProvider.validateToken(token)){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
        if(tokenRepository.isAccessTokenInBlacklist(token)){
            throw new AuthException(AuthErrorType.IS_BLACKED_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        String serviceType = jwtTokenProvider.getServiceTypeFromToken(token);
    }

    private boolean shouldSkipFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if(method.equals("OPTIONS")){
            return true;
        }

        return EXCLUDED_PATHS.stream()
                .anyMatch(requestURI::startsWith);
    }
}
