package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class DoctorReader {

    private final DoctorRepository doctorRepository;

    public DoctorReader(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        return doctorRepository.getAppointmentsByDoctor(doctor);
    }

    public Doctor findById(Long doctorId){
        return doctorRepository.findById(doctorId).orElseThrow(() -> new CoreException(CoreErrorType.DOCTOR_NOT_FOUND));
    }

    public Optional<Doctor> findByOAuthInfo(String oauthId, String provider){
        return doctorRepository.findByOAuthInfo(oauthId, provider);
    }
}
