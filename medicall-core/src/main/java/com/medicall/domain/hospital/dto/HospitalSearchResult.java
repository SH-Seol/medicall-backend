package com.medicall.domain.hospital.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.medicall.domain.department.Department;

import com.medicall.domain.hospital.Hospital;

public record HospitalSearchResult(
        Long id,
        String name,
        String imageUrl,
        double distance,
        String businessStatus,
        List<String> departments
) {
    public static HospitalSearchResult of(Hospital hospital, double distance){
        return new HospitalSearchResult(
                hospital.id(),
                hospital.name(),
                hospital.imageUrl(),
                Math.round(distance * 100.0) / 100.0,
                hospital.resolveBusinessStatus(LocalDateTime.now()).name(),
                hospital.departments() != null
                        ? hospital.departments().stream().map(Department::name).toList()
                        : List.of()
        );
    }
}
