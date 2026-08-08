package com.medicall.domain.appointment;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.appointment.dto.AvailableSlotResult;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorReader;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.HospitalReader;
import com.medicall.domain.hospital.OperatingTime;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.appointment.dto.AppointmentListResult;
import com.medicall.domain.appointment.dto.CreateAppointmentResult;
import com.medicall.domain.appointment.dto.DoctorAppointmentListCriteria;
import com.medicall.domain.appointment.dto.HospitalAppointmentListCriteria;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;

@Service
public class AppointmentService {

    private final AppointmentReader appointmentReader;
    private final AppointmentWriter appointmentWriter;
    private final AppointmentValidator appointmentValidator;
    private final DoctorReader doctorReader;
    private final HospitalReader hospitalReader;

    public AppointmentService(AppointmentReader appointmentReader, AppointmentWriter appointmentWriter,
                              AppointmentValidator appointmentValidator,
                              DoctorReader doctorReader, HospitalReader hospitalReader) {
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
        this.appointmentValidator = appointmentValidator;
        this.doctorReader = doctorReader;
        this.hospitalReader = hospitalReader;
    }

    @Transactional(readOnly = true)
    public CursorPageResult<Appointment> getAppointmentListByPatient(PatientAppointmentListCriteria criteria) {
        return appointmentReader.findByPatientId(criteria);
    }

    /**
     * 특정 의사의 하루 예약 가능 슬롯 조회.
     * 병원 운영 시간(휴게 시간 제외) 안에서 1시간 단위로 만들고,
     * 이미 예약이 있거나 지난 시간은 제외한다.
     */
    @Transactional(readOnly = true)
    public List<AvailableSlotResult> getAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorReader.findById(doctorId);
        if(doctor.hospital() == null){
            throw new CoreException(CoreErrorType.DOCTOR_NOT_BELONGS_TO_HOSPITAL);
        }

        Hospital hospital = hospitalReader.findById(doctor.hospital().id());
        OperatingTime operatingTime = hospital.weeklySchedule().stream()
                .filter(time -> time.dayOfWeek() == date.getDayOfWeek())
                .findFirst()
                .orElse(null);

        if(operatingTime == null || operatingTime.isClosed()){
            return List.of();
        }

        Set<LocalDateTime> reserved = Set.copyOf(appointmentReader.findActiveReservationTimes(
                doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay()));

        LocalDateTime now = LocalDateTime.now();
        List<AvailableSlotResult> slots = new ArrayList<>();

        // 정시 단위로만 예약을 받으므로 운영 시작 시각을 정시로 올려 시작한다.
        LocalTime cursor = operatingTime.openingTime().withMinute(0).withSecond(0).withNano(0);
        if(cursor.isBefore(operatingTime.openingTime())){
            cursor = cursor.plusHours(1);
        }

        while(!cursor.plusHours(1).isAfter(operatingTime.closingTime())){
            LocalDateTime slotTime = LocalDateTime.of(date, cursor);
            slots.add(resolveSlot(slotTime, cursor, operatingTime, reserved, now));

            cursor = cursor.plusHours(1);
        }

        return slots;
    }

    private AvailableSlotResult resolveSlot(LocalDateTime slotTime, LocalTime cursor,
                                            OperatingTime operatingTime, Set<LocalDateTime> reserved,
                                            LocalDateTime now) {
        if(slotTime.isBefore(now)){
            return AvailableSlotResult.unavailable(slotTime, "지난 시간");
        }
        if(isInBreak(cursor, operatingTime)){
            return AvailableSlotResult.unavailable(slotTime, "휴게 시간");
        }
        if(reserved.contains(slotTime)){
            return AvailableSlotResult.unavailable(slotTime, "이미 예약됨");
        }
        return AvailableSlotResult.available(slotTime);
    }

    private boolean isInBreak(LocalTime cursor, OperatingTime operatingTime) {
        if(operatingTime.breakStartTime() == null || operatingTime.breakFinishTime() == null){
            return false;
        }
        // 슬롯이 휴게 시간과 조금이라도 겹치면 예약을 받지 않는다.
        return cursor.isBefore(operatingTime.breakFinishTime())
                && cursor.plusHours(1).isAfter(operatingTime.breakStartTime());
    }

    @Transactional(readOnly = true)
    public Appointment findAppointmentByPatient(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validatePatientAccess(appointment, patientId);

        return appointment;
    }

    @Transactional
    public CreateAppointmentResult createAppointment(Long patientId, NewAppointment newAppointment){
        appointmentValidator.validateAppointmentCreation(patientId, newAppointment);
        Appointment appointment = appointmentWriter.create(patientId, newAppointment);

        return CreateAppointmentResult.from(appointment);
    }

    /**
     * 환자 예약 취소 (본인 예약만, 요청/배정 상태에서만 가능)
     */
    @Transactional
    public void cancelAppointmentByPatient(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validatePatientAccess(appointment, patientId);

        appointmentWriter.cancelAppointment(appointmentId, patientId);
    }

    /**
     * 의사가 환자에게 출발한다. ("다음 환자")
     * 이 시점부터 환자에게 위치를 공개할 수 있다.
     */
    @Transactional
    public AppointmentDetailResult departByDoctor(Long appointmentId, Long doctorId) {
        return changeStatusByDoctor(appointmentId, doctorId, AppointmentStatus.ASSIGNED, AppointmentStatus.EN_ROUTE);
    }

    /**
     * 의사가 환자 위치에 도착했다.
     */
    @Transactional
    public AppointmentDetailResult arriveByDoctor(Long appointmentId, Long doctorId) {
        return changeStatusByDoctor(appointmentId, doctorId, AppointmentStatus.EN_ROUTE, AppointmentStatus.ARRIVED);
    }

    /**
     * 진료를 시작한다.
     */
    @Transactional
    public AppointmentDetailResult startTreatmentByDoctor(Long appointmentId, Long doctorId) {
        return changeStatusByDoctor(appointmentId, doctorId, AppointmentStatus.ARRIVED, AppointmentStatus.IN_PROGRESS);
    }

    /**
     * 진료를 완료한다.
     */
    @Transactional
    public AppointmentDetailResult completeByDoctor(Long appointmentId, Long doctorId) {
        return changeStatusByDoctor(appointmentId, doctorId, AppointmentStatus.IN_PROGRESS, AppointmentStatus.COMPLETED);
    }

    private AppointmentDetailResult changeStatusByDoctor(Long appointmentId, Long doctorId,
                                                         AppointmentStatus expected, AppointmentStatus next) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateDoctorAccess(appointment, doctorId);

        appointmentWriter.updateStatusByDoctor(appointmentId, doctorId, expected, next);

        return AppointmentDetailResult.from(appointmentReader.findById(appointmentId));
    }

    @Transactional(readOnly = true)
    public AppointmentDetailResult findAppointmentByDoctor(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateDoctorAccess(appointment, doctorId);

        return AppointmentDetailResult.from(appointment);
    }

    @Transactional(readOnly = true)
    public CursorPageResult<AppointmentListResult> getAppointmentListByDoctor(DoctorAppointmentListCriteria criteria) {
        return appointmentReader.getAppointmentListByDoctor(criteria);
    }

    @Transactional(readOnly = true)
    public AppointmentDetailResult findAppointmentByHospital(Long hospitalId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateHospitalAccess(appointment, hospitalId);

        return AppointmentDetailResult.from(appointment);
    }

    @Transactional(readOnly = true)
    public CursorPageResult<AppointmentListResult> getAppointmentListByHospital(HospitalAppointmentListCriteria criteria) {
        return appointmentReader.getAppointmentListByHospital(criteria);
    }

    @Transactional
    public AppointmentDetailResult acceptAppointmentByHospital(Long appointmentId, Long hospitalId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateHospitalAccess(appointment, hospitalId);

        appointmentWriter.acceptAppointment(appointmentId, hospitalId);

        return AppointmentDetailResult.from(appointmentReader.findById(appointmentId));
    }

    /**
     * 병원이 예약을 거절한다.
     */
    @Transactional
    public void rejectAppointmentByHospital(Long appointmentId, Long hospitalId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateHospitalAccess(appointment, hospitalId);

        appointmentWriter.rejectAppointment(appointmentId, hospitalId);
    }

    /**
     * 병원이 의사 미지정 예약에 의사를 배정한다.
     */
    @Transactional
    public AppointmentDetailResult assignDoctorByHospital(Long appointmentId, Long hospitalId, Long doctorId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateHospitalAccess(appointment, hospitalId);

        appointmentWriter.assignDoctorToAppointment(appointmentId, hospitalId, doctorId);

        return AppointmentDetailResult.from(appointmentReader.findById(appointmentId));
    }
}
