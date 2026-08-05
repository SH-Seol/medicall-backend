package com.medicall.common.security;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

/**
 * access/refresh 토큰 발급·재발급·폐기를 담당한다.
 * refresh token은 jti 기준으로 Redis에 저장되며, 재발급 시 회전(rotation)된다.
 */
@Service
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    public TokenService(JwtTokenProvider jwtTokenProvider, TokenRepository tokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }

    /**
     * 로그인 성공 시 토큰 발급. refresh token은 Redis에 저장해 이후 폐기가 가능하도록 한다.
     */
    public TokenPair issue(Long userId, String serviceType) {
        String accessToken = jwtTokenProvider.generateAccessToken(userId, serviceType);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, serviceType);

        tokenRepository.saveRefreshToken(
                jwtTokenProvider.getJwtIdFromToken(refreshToken),
                userId,
                jwtTokenProvider.getRefreshTokenDuration()
        );

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * refresh token으로 토큰 재발급.
     * 사용된 refresh token은 즉시 폐기하고 새 refresh token을 발급한다.(회전)
     */
    public TokenPair reissue(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)){
            throw new AuthException(AuthErrorType.TOKEN_NOT_PROVIDED);
        }
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
        if(!TOKEN_TYPE_REFRESH.equals(jwtTokenProvider.getTokenTypeFromToken(refreshToken))){
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }

        String jwtId = jwtTokenProvider.getJwtIdFromToken(refreshToken);

        // 조회와 삭제가 원자적으로 이루어져 동시 재발급 요청은 한 번만 성공한다.
        Long userId = tokenRepository.consumeRefreshToken(jwtId)
                .orElseThrow(() -> {
                    // 서명은 유효한데 저장소에 없다 = 이미 사용된 토큰의 재사용
                    Long tokenUserId = jwtTokenProvider.getUserIdFromToken(refreshToken);
                    tokenRepository.deleteAllRefreshTokensByUserId(tokenUserId);

                    return new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
                });

        if(!userId.equals(jwtTokenProvider.getUserIdFromToken(refreshToken))){
            // 저장된 매핑과 토큰의 subject가 다르면 위조로 판단한다.
            throw new AuthException(AuthErrorType.INVALID_REFRESH_TOKEN);
        }

        String serviceType = jwtTokenProvider.getServiceTypeFromToken(refreshToken);

        log.debug("토큰 재발급 - userId: {}, serviceType: {}", userId, serviceType);

        return issue(userId, serviceType);
    }

    /**
     * 로그아웃. access token은 남은 유효시간만큼 블랙리스트에 등록하고 refresh token은 삭제한다.
     */
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)){
            Duration remaining = jwtTokenProvider.getRemainingDuration(accessToken);
            if(!remaining.isZero()){
                tokenRepository.addAccessTokenToBlacklist(jwtTokenProvider.getJwtIdFromToken(accessToken), remaining);
            }
        }

        if(StringUtils.hasText(refreshToken) && jwtTokenProvider.validateToken(refreshToken)){
            tokenRepository.deleteRefreshToken(jwtTokenProvider.getJwtIdFromToken(refreshToken));
        }
    }
}
