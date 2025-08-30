package com.medicall.domain.appointment;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
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
}
