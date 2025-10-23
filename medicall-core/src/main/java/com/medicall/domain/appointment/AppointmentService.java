package com.medicall.domain.appointment;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.appointment.dto.AppointmentListResult;
import com.medicall.domain.appointment.dto.CreateAppointmentResult;
import com.medicall.domain.appointment.dto.DoctorAppointmentListCriteria;
import com.medicall.domain.appointment.dto.HospitalAppointmentListCriteria;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentReader appointmentReader;
    private final AppointmentWriter appointmentWriter;
    private final AppointmentValidator appointmentValidator;

    public AppointmentService(AppointmentReader appointmentReader, AppointmentWriter appointmentWriter, AppointmentValidator appointmentValidator) {
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
        this.appointmentValidator = appointmentValidator;
    }

    public CursorPageResult<Appointment> getAppointmentListByPatient(PatientAppointmentListCriteria criteria) {
        return appointmentReader.findByPatientId(criteria);
    }

    public Appointment findAppointmentByPatient(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validatePatientAccess(appointment, patientId);

        return appointment;
    }

    public CreateAppointmentResult createAppointment(Long patientId, NewAppointment newAppointment){
        appointmentValidator.validateAppointmentCreation(patientId, newAppointment);
        Appointment appointment = appointmentWriter.create(patientId, newAppointment);

        return CreateAppointmentResult.from(appointment);
    }

    public AppointmentDetailResult findAppointmentByDoctor(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateDoctorAccess(appointment, doctorId);

        return AppointmentDetailResult.from(appointment);
    }

    public CursorPageResult<AppointmentListResult> getAppointmentListByDoctor(DoctorAppointmentListCriteria criteria) {
        return appointmentReader.getAppointmentListByDoctor(criteria);
    }

    public AppointmentDetailResult findAppointmentByHospital(Long hospitalId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validateHospitalAccess(appointment, hospitalId);

        return AppointmentDetailResult.from(appointment);
    }

    public CursorPageResult<AppointmentListResult> getAppointmentListByHospital(HospitalAppointmentListCriteria criteria) {
        return appointmentReader.getAppointmentListByHospital(criteria);
    }

}
