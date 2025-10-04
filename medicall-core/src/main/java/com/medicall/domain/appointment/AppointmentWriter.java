package com.medicall.domain.appointment;

import com.medicall.domain.doctor.Doctor;

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
}
