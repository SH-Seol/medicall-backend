package com.medicall.common.security;

import java.time.Duration;
import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private final String accessTokenName;
    private final String refreshTokenName;
    private final String domain;
    private final boolean secure;
    private final String sameSite;
    private final int accessTokenMaxAge;
    private final int refreshTokenMaxAge;

    public CookieManager(
            @Value("${cookie.access-token-name:accessToken}") String accessTokenName,
            @Value("${cookie.refresh-token-name:refreshToken}") String refreshTokenName,
            @Value("${cookie.domain:}") String domain,
            @Value("${cookie.secure:false}") boolean secure,
            @Value("${cookie.same-site:Lax}") String sameSite,
            @Value("${jwt.access-token-expiration}") int accessTokenMaxAge,
            @Value("${jwt.refresh-token-expiration}") int refreshTokenMaxAge
    ) {
        this.accessTokenName = accessTokenName;
        this.refreshTokenName = refreshTokenName;
        this.domain = domain;
        this.secure = secure;
        this.sameSite = sameSite;
        this.accessTokenMaxAge = accessTokenMaxAge;
        this.refreshTokenMaxAge = refreshTokenMaxAge;
    }

    public ResponseCookie createAccessTokenCookie(String accessToken){
        return createCookie(accessTokenName, accessToken, accessTokenMaxAge);
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken){
        return createCookie(refreshTokenName, refreshToken, refreshTokenMaxAge);
    }

    /**
     * 요청 쿠키에서 access token 조회 (없으면 null)
     */
    public String readAccessToken(HttpServletRequest request){
        return readCookie(request, accessTokenName);
    }

    /**
     * 요청 쿠키에서 refresh token 조회 (없으면 null)
     */
    public String readRefreshToken(HttpServletRequest request){
        return readCookie(request, refreshTokenName);
    }

    /**
     * 로그아웃 시 브라우저의 토큰 쿠키를 즉시 만료시킨다.
     */
    public ResponseCookie expireAccessTokenCookie(){
        return createCookie(accessTokenName, "", 0);
    }

    public ResponseCookie expireRefreshTokenCookie(){
        return createCookie(refreshTokenName, "", 0);
    }

    private String readCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * SameSite 지정이 필요해 ResponseCookie를 사용한다. (jakarta Cookie에는 SameSite setter가 없다)
     * 프론트와 API가 다른 등록 도메인에 배포되면 same-site 값을 None으로 두고 secure를 켜야 한다.
     */
    private ResponseCookie createCookie(String name, String value, int maxAge){
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .maxAge(Duration.ofSeconds(maxAge));

        // 도메인을 지정하지 않으면 요청 호스트에만 쿠키가 내려간다. (로컬 개발 기본값)
        if(domain != null && !domain.isBlank()){
            builder.domain(domain);
        }

        return builder.build();
    }
}
