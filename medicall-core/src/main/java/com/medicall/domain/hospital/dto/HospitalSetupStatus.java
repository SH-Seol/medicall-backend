package com.medicall.domain.hospital.dto;

import com.medicall.domain.hospital.Hospital;

/**
 * 병원 온보딩 진행 상태. 프론트에서 남은 단계를 안내하는 데 사용한다.
 */
public record HospitalSetupStatus(
        boolean addressRegistered,
        boolean departmentsRegistered,
        boolean operatingTimesRegistered,
        boolean completed
) {
    public static HospitalSetupStatus from(Hospital hospital) {
        boolean address = hospital.address() != null;
        boolean departments = hospital.departments() != null && !hospital.departments().isEmpty();
        boolean operatingTimes = hospital.weeklySchedule() != null && !hospital.weeklySchedule().isEmpty();

        return new HospitalSetupStatus(
                address,
                departments,
                operatingTimes,
                hospital.isSetUpComplete()
        );
    }
}
