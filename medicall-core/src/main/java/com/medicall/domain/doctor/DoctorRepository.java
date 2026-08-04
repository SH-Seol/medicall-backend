package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.doctor.dto.DoctorProfileUpdate;

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
    Doctor updateProfile(Long doctorId, DoctorProfileUpdate profileUpdate);
    void registerHospital(Long doctorId, Long hospitalId);
    List<Doctor> findAllByHospitalId(Long hospitalId);
}
