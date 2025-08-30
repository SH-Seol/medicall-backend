package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    Long save(Doctor newDoctor);
    List<Appointment> getAppointmentsByDoctor(Doctor doctor);
    Optional<Doctor> findById(Long id);
    boolean isDoctorBelongsToHospital(Long doctorId);
}
