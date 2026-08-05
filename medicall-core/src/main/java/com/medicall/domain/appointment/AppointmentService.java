package com.medicall.domain.appointment;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.appointment.dto.AppointmentDetailResult;
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

    public AppointmentService(AppointmentReader appointmentReader, AppointmentWriter appointmentWriter, AppointmentValidator appointmentValidator) {
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
        this.appointmentValidator = appointmentValidator;
    }

    @Transactional(readOnly = true)
    public CursorPageResult<Appointment> getAppointmentListByPatient(PatientAppointmentListCriteria criteria) {
        return appointmentReader.findByPatientId(criteria);
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
