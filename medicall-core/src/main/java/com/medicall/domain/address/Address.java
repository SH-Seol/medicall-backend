package com.medicall.domain.address;

public record Address(
    String zoneCode,
    String roadAddress,
    String jibunAddress,
    String detailAddress,
    String buildingName,
    Double longitude,
    Double latitude,
    boolean isDefault
) {
}
