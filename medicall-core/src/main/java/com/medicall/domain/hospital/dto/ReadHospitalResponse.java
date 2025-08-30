package com.medicall.domain.hospital.dto;

import com.medicall.domain.address.Address;
import com.medicall.domain.department.Department;
import com.medicall.domain.hospital.OperatingTime;
import java.util.List;

public record ReadHospitalResponse(
        String name,
        String telephoneNumber,
        Address address,
        String imageUrl,
        List<Department> departments,
        List<OperatingTime> weeklySchedule
) {
}
