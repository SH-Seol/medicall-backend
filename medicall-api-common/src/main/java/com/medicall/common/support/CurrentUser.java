package com.medicall.common.support;

public record CurrentUser(
        Long userId,
        String name,
        String serviceType // doctor, patient, hospital
) {
}
