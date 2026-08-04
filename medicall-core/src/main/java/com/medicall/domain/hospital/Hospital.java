package com.medicall.domain.hospital;

import com.medicall.domain.address.Address;
import com.medicall.domain.common.enums.BusinessStatus;
import com.medicall.domain.department.Department;

import java.time.LocalDateTime;
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
    /**
     * 요청 시각 기준 영업 상태를 계산한다.
     * businessStatus 필드는 저장되지 않는 값(@Transient)이라 조회 시점에 산출해야 한다.
     */
    public BusinessStatus resolveBusinessStatus(LocalDateTime now){
        if(weeklySchedule == null || weeklySchedule.isEmpty()){
            return BusinessStatus.CLOSED;
        }

        return weeklySchedule.stream()
                .filter(operatingTime -> operatingTime.dayOfWeek() == now.getDayOfWeek())
                .findFirst()
                .map(operatingTime -> operatingTime.statusAt(now.toLocalTime()))
                .orElse(BusinessStatus.CLOSED);
    }

    public boolean isSetUpComplete(){
        return address != null &&
                departments != null && !departments.isEmpty() &&
                weeklySchedule != null && !weeklySchedule.isEmpty();
    }
}
