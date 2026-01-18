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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

@Component
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
            "/auth/login",
            "/swagger-ui",
            "/v3/api-docs",
            "/api/v1/doctor/auth/dev-login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("Request URI: {}", request.getRequestURI());
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
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
        {
          "code": "UNAUTHORIZED",
          "message": "Invalid or expired token"
        }
        """);
            return;
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

        CustomUserDetails userDetails =
                new CustomUserDetails(userId, serviceType);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
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
