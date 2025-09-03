package com.medicall.domain.appointment;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository {
    Optional<Appointment> findById(Long appointmentId);
    void assignDoctorToAppointment(Appointment appointmentWithDoctor);
}
