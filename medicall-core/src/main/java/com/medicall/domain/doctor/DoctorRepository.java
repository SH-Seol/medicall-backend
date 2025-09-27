package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository {
    Doctor save(Doctor newDoctor);
    List<Appointment> getAppointmentsByDoctor(Doctor doctor);
    Optional<Doctor> findById(Long id);
    boolean isDoctorBelongsToHospital(Long doctorId);
    Optional<Doctor> findByOAuthInfo(String oauthId, String provider);
    boolean isDoctorExist(Long doctorId);
}
