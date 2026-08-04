package com.medicall.common.security;

import java.util.Arrays;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private final String accessTokenName;
    private final String refreshTokenName;
    private final String domain;
    private final boolean secure;
    private final int accessTokenMaxAge;
    private final int refreshTokenMaxAge;

    public CookieManager(
            @Value("${cookie.access-token-name:accessToken}") String accessTokenName,
            @Value("${cookie.refresh-token-name:refreshToken}") String refreshTokenName,
            @Value("${cookie.domain:}") String domain,
            @Value("${cookie.secure:false}") boolean secure,
            @Value("${jwt.access-token-expiration}") int accessTokenMaxAge,
            @Value("${jwt.refresh-token-expiration}") int refreshTokenMaxAge
    ) {
        this.accessTokenName = accessTokenName;
        this.refreshTokenName = refreshTokenName;
        this.domain = domain;
        this.secure = secure;
        this.accessTokenMaxAge = accessTokenMaxAge;
        this.refreshTokenMaxAge = refreshTokenMaxAge;
    }

    public Cookie createAccessTokenCookie(String accessToken){
        return createCookie(accessTokenName, accessToken, accessTokenMaxAge);
    }

    public Cookie createRefreshTokenCookie(String refreshToken){
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
    public Cookie expireAccessTokenCookie(){
        return createCookie(accessTokenName, "", 0);
    }

    public Cookie expireRefreshTokenCookie(){
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

    private Cookie createCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setMaxAge(maxAge);
        // 도메인을 지정하지 않으면 요청 호스트에만 쿠키가 내려간다. (로컬 개발 기본값)
        if(domain != null && !domain.isBlank()){
            cookie.setDomain(domain);
        }
        return cookie;
    }
}
