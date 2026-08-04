package com.medicall.storage.redis.prescription;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.medicall.domain.prescription.PrescriptionQrTokenStore;

/**
 * 처방전 QR 토큰을 Redis에 보관한다. (토큰 -> 처방전 id, 5분 유효)
 */
@Repository
public class PrescriptionQrTokenRedisStore implements PrescriptionQrTokenStore {

    private static final String KEY_PREFIX = "prescription_qr:";
    private static final Duration TTL = Duration.ofMinutes(5);
    private static final int TOKEN_BYTES = 24;

    private final StringRedisTemplate redisTemplate;
    private final SecureRandom random = new SecureRandom();

    public PrescriptionQrTokenRedisStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String issue(Long prescriptionId) {
        byte[] bytes = new byte[TOKEN_BYTES];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        redisTemplate.opsForValue().set(KEY_PREFIX + token, prescriptionId.toString(), TTL);

        return token;
    }

    @Override
    public Optional<Long> resolve(String qrToken) {
        String prescriptionId = redisTemplate.opsForValue().get(KEY_PREFIX + qrToken);

        return Optional.ofNullable(prescriptionId).map(Long::valueOf);
    }
}
