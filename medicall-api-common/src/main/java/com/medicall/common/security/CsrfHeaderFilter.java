package com.medicall.common.security;

import java.io.IOException;
import java.util.Set;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * CSRF 방어.
 * 인증을 HttpOnly 쿠키로도 처리하므로 상태 변경 요청에 커스텀 헤더를 요구한다.
 * 커스텀 헤더가 붙으면 브라우저가 preflight를 보내고, CORS 화이트리스트가 오리진을 검사한다.
 * 공격자 사이트는 폼 전송으로 커스텀 헤더를 붙일 수 없어 위조 요청이 차단된다.
 */
@Component
public class CsrfHeaderFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CsrfHeaderFilter.class);

    public static final String CLIENT_HEADER = "X-Medicall-Client";

    private static final Set<String> STATE_CHANGING_METHODS = Set.of("POST", "PUT", "PATCH", "DELETE");

    /** OAuth 로그인 리다이렉트 등 브라우저가 직접 이동하는 경로는 헤더를 붙일 수 없다. */
    private static final Set<String> EXCLUDED_PATTERNS = Set.of(
            "/oauth2/**",
            "/login/**"
    );

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final boolean enabled;

    public CsrfHeaderFilter(@Value("${security.csrf-header.enabled:true}") boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!enabled || !isStateChanging(request) || isExcluded(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!StringUtils.hasText(request.getHeader(CLIENT_HEADER))) {
            log.warn("클라이언트 헤더 없는 상태 변경 요청 차단 - {} {}", request.getMethod(), request.getRequestURI());

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                    {
                      "code": "COMMON-403",
                      "message": "허용되지 않은 요청입니다."
                    }
                    """);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isStateChanging(HttpServletRequest request) {
        return STATE_CHANGING_METHODS.contains(request.getMethod());
    }

    private boolean isExcluded(HttpServletRequest request) {
        return EXCLUDED_PATTERNS.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, request.getRequestURI()));
    }
}
