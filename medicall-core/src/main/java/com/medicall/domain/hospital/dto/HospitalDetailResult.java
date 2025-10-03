package com.medicall.domain.hospital.dto;

import java.util.List;

import com.medicall.domain.address.Address;
import com.medicall.domain.department.Department;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.OperatingTime;

public record HospitalDetailResult(
        Long id,
        String name,
        String telephoneNumber,
        Address address,
        String imageUrl,
        List<String> departments,
        List<OperatingTime> weeklySchedule,
        String businessStatus,
        double distance
) {
    public static HospitalDetailResult of(Hospital hospital, double distance) {
        return new HospitalDetailResult(
                hospital.id(),
                hospital.name(),
                hospital.telephoneNumber(),
                hospital.address(),
                hospital.imageUrl(),
                hospital.departments().stream().map(Department::name).toList(),
                hospital.weeklySchedule(),
                hospital.businessStatus().name(),
                Math.round(distance * 100.0) / 100.0
        );
    }
}
