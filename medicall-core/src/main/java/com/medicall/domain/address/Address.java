package com.medicall.domain.address;

public record Address(
    Long id,
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
