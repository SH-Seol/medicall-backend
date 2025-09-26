package com.medicall.domain.appointment;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.support.CursorPageResult;

import org.springframework.stereotype.Component;

@Component
public class AppointmentReader {

    private final AppointmentRepository appointmentRepository;

    public AppointmentReader(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new CoreException(CoreErrorType.APPOINTMENT_NOT_FOUND));
    }

    public CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria) {
        return appointmentRepository.findByPatientId(criteria);
    }
}
