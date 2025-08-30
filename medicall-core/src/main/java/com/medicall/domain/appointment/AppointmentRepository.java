package com.medicall.domain.appointment;

import java.util.Optional;

public interface AppointmentRepository {
    Optional<Appointment> findById(Long appointmentId);
    void assignDoctorToAppointment(Appointment appointmentWithDoctor);
}
