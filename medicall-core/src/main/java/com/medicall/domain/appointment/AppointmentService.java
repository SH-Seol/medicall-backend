package com.medicall.domain.appointment;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentReader appointmentReader;
    private final AppointmentWriter appointmentWriter;

    public AppointmentService(AppointmentReader appointmentReader, AppointmentWriter appointmentWriter) {
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
    }

    public CursorPageResult<Appointment> getAppointmentList(PatientAppointmentListCriteria criteria) {
        return appointmentReader.findByPatientId(criteria);
    }

}
