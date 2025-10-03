package com.medicall.domain.hospital.dto;

import com.medicall.domain.hospital.Hospital;

public record HospitalSearchResult(
        Long id,
        String name,
        String imageUrl,
        double distance,
        String businessStatus
) {
    public static HospitalSearchResult of(Hospital hospital, double distance){
        return new HospitalSearchResult(
                hospital.id(),
                hospital.name(),
                hospital.imageUrl(),
                Math.round(distance * 100.0) / 100.0,
                hospital.businessStatus().name()
        );
    }
}
