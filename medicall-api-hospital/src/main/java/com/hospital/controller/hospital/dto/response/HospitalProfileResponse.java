package com.hospital.controller.hospital.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.medicall.domain.address.Address;
import com.medicall.domain.hospital.OperatingTime;
import com.medicall.domain.hospital.dto.HospitalProfileResult;

@Schema(description = "병원 내 정보 응답")
public record HospitalProfileResponse(
        Long id,
        String name,
        String telephoneNumber,
        String imageUrl,
        Address address,
        List<String> departments,
        List<OperatingTimeResponse> weeklySchedule
) {
    public static HospitalProfileResponse from(HospitalProfileResult result) {
        return new HospitalProfileResponse(
                result.id(),
                result.name(),
                result.telephoneNumber(),
                result.imageUrl(),
                result.address(),
                result.departments(),
                result.weeklySchedule().stream().map(OperatingTimeResponse::from).toList()
        );
    }

    public record OperatingTimeResponse(
            DayOfWeek dayOfWeek,
            boolean isClosed,
            LocalTime openingTime,
            LocalTime closingTime,
            LocalTime breakStartTime,
            LocalTime breakFinishTime
    ) {
        public static OperatingTimeResponse from(OperatingTime operatingTime) {
            return new OperatingTimeResponse(
                    operatingTime.dayOfWeek(),
                    operatingTime.isClosed(),
                    operatingTime.openingTime(),
                    operatingTime.closingTime(),
                    operatingTime.breakStartTime(),
                    operatingTime.breakFinishTime()
            );
        }
    }
}
