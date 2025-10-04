package com.patient.controller.v1.hospital.dto.response;

import java.util.List;

import com.medicall.domain.address.Address;
import com.medicall.domain.hospital.OperatingTime;
import com.medicall.domain.hospital.dto.HospitalDetailResult;

public record PatientHospitalDetailResponse(
        Long id,
        String name,
        String telephoneNumber,
        Address address,
        double distance,
        String imageUrl,
        List<String> departments,
        List<OperatingTime> weeklySchedule,
        String businessStatus
) {
    public static PatientHospitalDetailResponse from(HospitalDetailResult result){
        return new PatientHospitalDetailResponse(
                result.id(),
                result.name(),
                result.telephoneNumber(),
                result.address(),
                result.distance(),
                result.imageUrl(),
                result.departments(),
                result.weeklySchedule(),
                result.businessStatus()
        );
    }
}
