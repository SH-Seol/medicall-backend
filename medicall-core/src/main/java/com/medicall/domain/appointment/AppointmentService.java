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
    private final AppointmentValidator appointmentValidator;

    public AppointmentService(AppointmentReader appointmentReader, AppointmentWriter appointmentWriter, AppointmentValidator appointmentValidator) {
        this.appointmentReader = appointmentReader;
        this.appointmentWriter = appointmentWriter;
        this.appointmentValidator = appointmentValidator;
    }

    public CursorPageResult<Appointment> getAppointmentList(PatientAppointmentListCriteria criteria) {
        return appointmentReader.findByPatientId(criteria);
    }

    public Appointment findByAppointmentId(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentReader.findById(appointmentId);
        appointmentValidator.validatePatient(appointment, patientId);

        return appointment;
    }

}
