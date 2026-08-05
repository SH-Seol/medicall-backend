package com.medicall.domain.appointment;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

import org.springframework.stereotype.Component;

@Component
public class AppointmentWriter {

    private final AppointmentRepository appointmentRepository;

    public AppointmentWriter(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * 아래 상태 전이들은 조건부 UPDATE로 처리되며, 갱신되지 않으면(다른 곳에서 상태가 바뀐 경우)
     * APPOINTMENT_STATUS_CHANGED 예외를 던진다.
     */
    public void assignDoctorToAppointment(Long appointmentId, Long hospitalId, Long doctorId) {
        if(!appointmentRepository.assignDoctorToAppointment(appointmentId, hospitalId, doctorId)){
            throw new CoreException(CoreErrorType.APPOINTMENT_STATUS_CHANGED);
        }
    }

    public void acceptAppointment(Long appointmentId, Long hospitalId) {
        if(!appointmentRepository.acceptAppointment(appointmentId, hospitalId)){
            throw new CoreException(CoreErrorType.APPOINTMENT_STATUS_CHANGED);
        }
    }

    public void cancelAppointment(Long appointmentId, Long patientId) {
        if(!appointmentRepository.cancelAppointment(appointmentId, patientId)){
            throw new CoreException(CoreErrorType.APPOINTMENT_NOT_CANCELABLE);
        }
    }

    public void rejectAppointment(Long appointmentId, Long hospitalId) {
        if(!appointmentRepository.rejectAppointment(appointmentId, hospitalId)){
            throw new CoreException(CoreErrorType.APPOINTMENT_STATUS_CHANGED);
        }
    }

    public Appointment create(Long patientId, NewAppointment newAppointment) {
        return appointmentRepository.create(patientId, newAppointment);
    }
}
