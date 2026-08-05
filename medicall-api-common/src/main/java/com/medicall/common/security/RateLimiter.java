package com.medicall.common.security;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis 기반 고정 윈도우 요청 제한.
 * 앱 인스턴스가 여러 대여도 카운트가 공유된다.
 */
@Component
public class RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    private static final String KEY_PREFIX = "rate_limit:";

    private final StringRedisTemplate redisTemplate;

    public RateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 시도 횟수를 1 늘리고 제한을 넘었는지 반환한다.
     *
     * @param key    제한 단위 (ex. "invitation:doctor:3")
     * @param limit  윈도우 내 허용 횟수
     * @param window 윈도우 길이
     * @return 제한을 초과했으면 true
     */
    public boolean isExceeded(String key, int limit, Duration window) {
        String redisKey = KEY_PREFIX + key;

        try{
            Long count = redisTemplate.opsForValue().increment(redisKey);

            if(count != null && count == 1L){
                redisTemplate.expire(redisKey, window);
            }

            boolean exceeded = count != null && count > limit;
            if(exceeded){
                log.warn("요청 제한 초과 - key: {}, count: {}", key, count);
            }

            return exceeded;
        }catch (Exception e){
            // Redis 장애로 정상 요청을 막지는 않는다.
            log.error("요청 제한 확인 실패 - key: {}", key, e);
            return false;
        }
    }
}
