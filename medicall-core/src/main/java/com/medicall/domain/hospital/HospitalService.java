package com.medicall.domain.hospital;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentReader;
import com.medicall.domain.appointment.AppointmentWriter;
import com.medicall.domain.hospital.dto.HospitalDetailResult;
import com.medicall.domain.hospital.dto.HospitalProfileResult;
import com.medicall.domain.hospital.dto.HospitalProfileUpdate;
import com.medicall.domain.hospital.dto.HospitalSearchCriteria;
import com.medicall.domain.hospital.dto.HospitalSetupStatus;
import com.medicall.domain.hospital.dto.HospitalSearchResult;
import com.medicall.domain.address.Address;
import com.medicall.domain.department.DepartmentReader;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorReader;
import com.medicall.support.CursorPageResult;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HospitalService {

    private final HospitalReader hospitalReader;
    private final HospitalWriter hospitalWriter;
    private final DoctorReader doctorReader;
    private final AppointmentReader appointmentReader;
    private final AppointmentWriter appointmentWriter;
    private final DepartmentReader departmentReader;

    public HospitalService(HospitalReader reader,
                    HospitalWriter writer,
                    DoctorReader doctorReader,
                    AppointmentReader appointmentReader,
                    AppointmentWriter appointmentWriter,
                    DepartmentReader departmentReader) {
        this.hospitalReader = reader;
        this.hospitalWriter = writer;
        this.doctorReader = doctorReader;
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
        this.departmentReader = departmentReader;
    }

    //예약 조회
    @Transactional(readOnly = true)
    public Optional<List<Appointment>> getAppointments(Long hospitalId) {
        return hospitalReader.getAppointments(hospitalId);
    }

    //병원 업무 시간 등록 주간 일괄 등록
    //병원 업무 시간 수정
    //공휴일 업무 여부 등록
    @Transactional
    public void updateOperatingTime(Long hospitalId, List<OperatingTime> operatingTimes) {
        hospitalWriter.updaterOperatingTimes(hospitalId, operatingTimes);
    }

    /**
     * 주변 병원 목록 조회 (이름 or 진료 과목)
     */
    @Transactional(readOnly = true)
    public CursorPageResult<HospitalSearchResult> getHospitalsNearby(HospitalSearchCriteria criteria) {
        return hospitalReader.searchNearby(criteria);
    }

    /**
     * 온보딩: 병원 주소 등록·수정
     */
    @Transactional
    public HospitalProfileResult updateAddress(Long hospitalId, Address address) {
        hospitalWriter.updateAddress(hospitalId, address);

        return HospitalProfileResult.from(hospitalReader.findById(hospitalId));
    }

    /**
     * 온보딩: 병원 진료과 등록 (전달한 목록으로 전체 교체)
     */
    @Transactional
    public HospitalProfileResult updateDepartments(Long hospitalId, List<Long> departmentIds) {
        departmentIds.forEach(departmentReader::findById);
        hospitalWriter.updateDepartments(hospitalId, departmentIds);

        return HospitalProfileResult.from(hospitalReader.findById(hospitalId));
    }

    /**
     * 온보딩 진행 상태 조회
     */
    @Transactional(readOnly = true)
    public HospitalSetupStatus getSetupStatus(Long hospitalId) {
        return HospitalSetupStatus.from(hospitalReader.findById(hospitalId));
    }

    /**
     * 병원 내 정보 조회
     */
    @Transactional(readOnly = true)
    public HospitalProfileResult getMyProfile(Long hospitalId) {
        return HospitalProfileResult.from(hospitalReader.findById(hospitalId));
    }

    /**
     * 병원 내 정보 수정 (null인 항목은 기존 값 유지)
     */
    @Transactional
    public HospitalProfileResult updateMyProfile(Long hospitalId, HospitalProfileUpdate profileUpdate) {
        return HospitalProfileResult.from(hospitalWriter.updateProfile(hospitalId, profileUpdate));
    }

    @Transactional(readOnly = true)
    public HospitalDetailResult findById(Long hospitalId, double lat, double lng) {
        return hospitalReader.findByIdWithLocation(hospitalId, lat, lng);
    }
}
