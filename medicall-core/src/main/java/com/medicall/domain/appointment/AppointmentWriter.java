package com.medicall.domain.appointment;

import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.domain.doctor.Doctor;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

import org.springframework.stereotype.Component;

@Component
public class AppointmentWriter {

    private final AppointmentRepository appointmentRepository;

    public AppointmentWriter(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void assignDoctorToAppointment(Doctor doctor, Appointment appointment) {
        Appointment appointmentWithDoctor = appointment.assignDoctor(doctor);
        appointmentRepository.assignDoctorToAppointment(appointmentWithDoctor);
    }

    public Appointment create(Long patientId, NewAppointment newAppointment) {
        return appointmentRepository.create(patientId, newAppointment);
    }

    public AppointmentDetailResult acceptAppointment(Appointment appointment){
        if(appointment.status().equals(AppointmentStatus.REQUESTED)){
            appointmentRepository.acceptAppointment(appointment);
        }
        else{
            throw new CoreException(CoreErrorType.APPOINTMENT_NOT_ACCEPTABLE);
        }
        return AppointmentDetailResult.from(appointment);
    }
}
