package com.medicall.domain.prescription;

import java.util.Optional;

/**
 * 처방전 QR 토큰 저장소.
 * 토큰은 짧은 유효 시간을 가지며 처방전 id로 되돌릴 수 있어야 한다.
 */
public interface PrescriptionQrTokenStore {
    String issue(Long prescriptionId);
    Optional<Long> resolve(String qrToken);
}
