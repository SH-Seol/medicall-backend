package com.medicall.common.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 각 서비스(auth) 컨트롤러가 상속해 재발급/로그아웃 엔드포인트를 공유한다.
 * 토큰은 HttpOnly 쿠키로 주고받으므로 요청 바디가 필요 없다.
 */
public abstract class AuthTokenController {

    protected final TokenService tokenService;
    protected final CookieManager cookieManager;

    protected AuthTokenController(TokenService tokenService, CookieManager cookieManager) {
        this.tokenService = tokenService;
        this.cookieManager = cookieManager;
    }

    @Operation(
            summary = "토큰 재발급",
            description = "refresh token 쿠키로 access/refresh token을 재발급합니다. 사용된 refresh token은 폐기됩니다.(회전)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "refresh token이 없거나 만료·폐기됨")
    })
    @PostMapping("/reissue")
    public ResponseEntity<Void> reissue(HttpServletRequest request, HttpServletResponse response) {
        TokenPair tokens = tokenService.reissue(cookieManager.readRefreshToken(request));

        response.addCookie(cookieManager.createAccessTokenCookie(tokens.accessToken()));
        response.addCookie(cookieManager.createRefreshTokenCookie(tokens.refreshToken()));

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그아웃",
            description = "access token을 남은 유효시간 동안 블랙리스트에 등록하고 refresh token을 폐기한 뒤 토큰 쿠키를 만료시킵니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "성공"))
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        tokenService.logout(
                resolveAccessToken(request),
                cookieManager.readRefreshToken(request)
        );

        response.addCookie(cookieManager.expireAccessTokenCookie());
        response.addCookie(cookieManager.expireRefreshTokenCookie());

        return ResponseEntity.noContent().build();
    }

    /**
     * access token은 Authorization 헤더를 우선하고, 없으면 쿠키에서 읽는다.
     */
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return cookieManager.readAccessToken(request);
    }
}
