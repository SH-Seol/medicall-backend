package com.medicall.domain.appointment;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class AppointmentValidator {

    public void validatePatient(Appointment appointment, Long patientId) {
        if(!appointment.patient().id().equals(patientId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }

    public void validateDoctor(Appointment appointment, Long doctorId) {
        if(!appointment.doctor().id().equals(doctorId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }

    public void validateHospital(Appointment appointment, Long hospitalId) {
        if(!appointment.hospital().id().equals(hospitalId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }
}
