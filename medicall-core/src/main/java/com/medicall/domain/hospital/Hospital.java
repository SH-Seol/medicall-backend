package com.medicall.domain.hospital;

import com.medicall.domain.address.Address;
import com.medicall.domain.common.enums.BusinessStatus;
import com.medicall.domain.department.Department;
import java.util.List;

public record Hospital(
        Long id,
        String name,
        String telephoneNumber,
        Address address,
        String imageUrl,
        List<Department> departments,
        List<OperatingTime> weeklySchedule,
        BusinessStatus businessStatus
) {
    public boolean isSetUpComplete(){
        return address != null &&
                departments != null &&
                weeklySchedule != null;
    }
}
