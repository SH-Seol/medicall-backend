package com.patient.controller.v1.address.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import com.medicall.domain.address.Address;

@Schema(description = "환자 주소")
public record PatientAddressResponse(
        Long addressId,
        String zoneCode,
        String roadAddress,
        String jibunAddress,
        String detailAddress,
        String buildingName,
        Double longitude,
        Double latitude,
        @Schema(description = "기본 주소 여부")
        boolean isDefault
) {
    public static PatientAddressResponse from(Address address) {
        return new PatientAddressResponse(
                address.id(),
                address.zoneCode(),
                address.roadAddress(),
                address.jibunAddress(),
                address.detailAddress(),
                address.buildingName(),
                address.longitude(),
                address.latitude(),
                address.isDefault()
        );
    }
}
