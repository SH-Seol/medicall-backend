package com.medicall.common.security;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private String accessTokenName;
    private String refreshTokenName;
    private String domain;
    private boolean secure;

    public Cookie createAccessTokenCookie(String accessToken){
        Cookie cookie = new Cookie(accessTokenName, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setDomain(domain);
        cookie.setMaxAge(60 * 60 * 24);
        return cookie;
    }

    public Cookie createRefreshTokenCookie(String refreshToken){
        Cookie cookie = new Cookie(refreshTokenName, refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(secure);
        cookie.setDomain(domain);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        return cookie;
    }

}
