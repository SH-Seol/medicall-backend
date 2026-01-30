package com.medicall.domain.hospital;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentReader;
import com.medicall.domain.appointment.AppointmentWriter;
import com.medicall.domain.hospital.dto.HospitalDetailResult;
import com.medicall.domain.hospital.dto.HospitalSearchCriteria;
import com.medicall.domain.hospital.dto.HospitalSearchResult;
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

    public HospitalService(HospitalReader reader,
                    HospitalWriter writer,
                    DoctorReader doctorReader,
                    AppointmentReader appointmentReader,
                    AppointmentWriter appointmentWriter) {
        this.hospitalReader = reader;
        this.hospitalWriter = writer;
        this.doctorReader = doctorReader;
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
    }

    //예약 조회
    @Transactional(readOnly = true)
    public Optional<List<Appointment>> getAppointments(Long hospitalId) {
        return hospitalReader.getAppointments(hospitalId);
    }

    //예약 거절
    @Transactional
    public void rejectAppointment(Long hospitalId, Long appointmentId) {
        hospitalWriter.rejectAppointment(hospitalId, appointmentId);
    }

    //의사 없는 요청 의사 배정
    @Transactional
    public Long designateDoctorToAppointment(Long hospitalId, Long doctorId, Long appointmentId) {
        Doctor doctor = doctorReader.findById(doctorId);
        Appointment appointment = appointmentReader.findById(appointmentId);
        if(!appointment.hospital().id().equals(hospitalId)){
            throw new IllegalArgumentException("병원의 예약이 아닙니다.");
        }
        if(appointment.doctor() != null){
            throw new IllegalArgumentException("이미 배정된 의사가 존재합니다.");
        }
        appointmentWriter.assignDoctorToAppointment(doctor, appointment);

        return appointment.id();
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

    @Transactional(readOnly = true)
    public HospitalDetailResult findById(Long hospitalId, double lat, double lng) {
        return hospitalReader.findByIdWithLocation(hospitalId, lat, lng);
    }
}
