package com.medicall.domain.hospital.dto;

import com.medicall.support.CorePageUtils;

public record HospitalSearchByNameCriteria(
        Long userId,
        String keyword,
        Long cursorId,
        int size,
        double lat,
        double lng
) {
    public boolean hasCursor() {
        return CorePageUtils.hasCursor(cursorId);
    }
}