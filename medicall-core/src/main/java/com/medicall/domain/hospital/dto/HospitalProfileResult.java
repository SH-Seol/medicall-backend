package com.medicall.domain.hospital.dto;

import java.util.List;

import com.medicall.domain.address.Address;
import com.medicall.domain.department.Department;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.OperatingTime;

public record HospitalProfileResult(
        Long id,
        String name,
        String telephoneNumber,
        String imageUrl,
        Address address,
        List<String> departments,
        List<OperatingTime> weeklySchedule
) {
    public static HospitalProfileResult from(Hospital hospital) {
        return new HospitalProfileResult(
                hospital.id(),
                hospital.name(),
                hospital.telephoneNumber(),
                hospital.imageUrl(),
                hospital.address(),
                hospital.departments() != null ? hospital.departments().stream().map(Department::name).toList() : List.of(),
                hospital.weeklySchedule() != null ? hospital.weeklySchedule() : List.of()
        );
    }
}
