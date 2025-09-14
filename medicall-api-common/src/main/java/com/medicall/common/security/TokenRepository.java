package com.medicall.common.security;

import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

@Repository
public class TokenRepository {

    private static final Logger log = LoggerFactory.getLogger(TokenRepository.class);
    private final RedisTemplate<String, String> redisTemplate;

    public TokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String USER_TOKEN_PREFIX = "user_token:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * Refresh Token 저장
     * @param jwtId JWT id (토큰 고유 식별자)
     * @param userId 사용자 id
     * @param duration 만료 시간
     */
    public void saveRefreshToken(String jwtId, Long userId, Duration duration) {
        try{
            String key = REFRESH_TOKEN_PREFIX + jwtId;
            // Refresh Token 저장 ( JWT id -> User id 매핑)
            redisTemplate.opsForValue().set(key, userId.toString(), duration);
            // 사용자별 활성 토큰 목록에 추가
            String userTokenKey = USER_TOKEN_PREFIX + userId;
            redisTemplate.opsForSet().add(userTokenKey, jwtId);
            redisTemplate.expire(userTokenKey, duration);

            log.debug("Refresh Token 저장 - jwtId: {}, userId: {}", jwtId, userId);
        }catch (Exception e){
            log.error("Refresh Token 저장 실패 - jwtId: {}, userId: {}", jwtId, userId);
            throw new AuthException(AuthErrorType.SAVING_REFRESH_TOKEN_FAILED);
        }
    }

    /**
     * Refresh Token으로 사용자 ID 조회
     * @param jwtId JWT id
     * @return User id (Optional)
     */
    public Optional<Long> findUserIdByRefreshToken(String jwtId) {
        try{
            String key = REFRESH_TOKEN_PREFIX + jwtId;
            String userId = redisTemplate.opsForValue().get(key);

            if(userId != null){
                log.debug("Refresh Token을 이용하여 user 조회 - jwtId: {}, userId: {}", jwtId, userId);
                return Optional.of(Long.valueOf(userId));
            }

            log.debug("Refresh Token 내 유저 없음 - jwtId: {}", jwtId);
            return Optional.empty();
        }catch (Exception e){
            log.error("Refresh Token을 이용한 유저 조회 실패 - jwtId: {}", jwtId, e);
            return Optional.empty();
        }
    }

    /**
     * Refresh Token 삭제
     * @param jwtId JWT id
     */
    public void deleteRefreshToken(String jwtId) {
        try{
            String key = REFRESH_TOKEN_PREFIX + jwtId;
            String userId = redisTemplate.opsForValue().get(key);

            if(userId != null){
                //Refresh Token 삭제
                redisTemplate.delete(key);

                //사용자 토큰 목록에서 제거
                String userTokenKey = USER_TOKEN_PREFIX + userId;
                redisTemplate.opsForSet().remove(userTokenKey, jwtId);

                log.debug("Refresh Token 삭제 - jwtId: {}, userId: {}", jwtId, userId);
            }
        }catch (Exception e){
            log.error("Refresh Token 삭제 실패 - jwtId: {}", jwtId, e);
        }
    }

    /**
     *
     * @param jwtId JWT id
     * @param remainingTime 남은 시간
     */
    public void addAccessTokenToBlacklist(String jwtId, Duration remainingTime) {
        try{
            String key = BLACKLIST_PREFIX + jwtId;
            redisTemplate.opsForValue().set(key, "blacklisted", remainingTime);

            log.debug("Access Token 블랙리스트에 추가 - jwtId: {}", jwtId);
        }catch (Exception e){
            log.error("Access Token 블랙리스트에 추가 실패 - jwtId: {}", jwtId, e);
        }
    }

    /**
     *
     * @param jwtId JWT id
     * @return access token이 블랙리스트에 있으면 true, 없으면 false
     */
    public boolean isAccessTokenInBlacklist(String jwtId) {
        try{
            String key = BLACKLIST_PREFIX + jwtId;
            Boolean isExist = redisTemplate.hasKey(key);

            boolean isBlacked = Boolean.TRUE.equals(isExist);

            if(isBlacked) {
                log.debug("Access Token이 이미 블랙 - jwtId: {}", jwtId);
            }

            return isBlacked;

        }catch (Exception e){
            log.error("블랙리스트 상태 조회 실패 - jwtId: {}", jwtId, e);
            return false;
        }
    }
}
