package com.medicall.domain.hospital;

import com.medicall.domain.appointment.Appointment;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository {
    Long save(NewHospital newHospital, List<OperatingTime> operatingTimes);
    List<Appointment> findAppointmentsByHospitalId(Long id);
    void rejectAppointmentById(Long hospitalId, Long appointmentId);
    Long addDoctorOnAppointment(Long doctorId, Long appointmentId);
    void updateOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes);
    Optional<Hospital> findById(Long hospitalId);
    List<Hospital> findAllByKeyword(String keyword);
    Optional<Hospital> findByOAuthInfo(String oauthId, String provider);
}
