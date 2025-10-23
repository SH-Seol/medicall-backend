package com.medicall.domain.appointment;

import java.util.ArrayList;
import java.util.List;

import com.medicall.domain.appointment.dto.AppointmentListResult;
import com.medicall.domain.appointment.dto.DoctorAppointmentListCriteria;
import com.medicall.domain.appointment.dto.HospitalAppointmentListCriteria;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;

import org.springframework.stereotype.Component;

@Component
public class AppointmentReader {

    private final AppointmentRepository appointmentRepository;

    public AppointmentReader(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new CoreException(CoreErrorType.APPOINTMENT_NOT_FOUND));
    }

    public CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria) {
        return appointmentRepository.findByPatientId(criteria);
    }

    public CursorPageResult<AppointmentListResult> getAppointmentListByDoctor(DoctorAppointmentListCriteria criteria) {
        List<Appointment> appointmentList = appointmentRepository.findAllByDoctorId(criteria.doctorId(),
                criteria.cursorId(), criteria.size());

        List<AppointmentListResult> result = appointmentList.stream().map(AppointmentListResult::from).toList();

        return CorePageUtils.buildCursorResult(result, criteria.size(), AppointmentListResult::appointmentId);
    }

    public CursorPageResult<AppointmentListResult> getAppointmentListByHospital(HospitalAppointmentListCriteria criteria) {
        List<Appointment> appointmentList = appointmentRepository.findAllByHospitalId(criteria.hospitalId(),
                criteria.cursorId(), criteria.size());

        List<AppointmentListResult> result = appointmentList.stream().map(AppointmentListResult::from).toList();

        return CorePageUtils.buildCursorResult(result, criteria.size(), AppointmentListResult::appointmentId);
    }
}
