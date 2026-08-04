package com.medicall.domain.doctor;

import org.springframework.stereotype.Component;

import com.medicall.domain.doctor.dto.DoctorProfileUpdate;

@Component
public class DoctorWriter {

    private final DoctorRepository doctorRepository;

    public DoctorWriter(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(Doctor newDoctor) {
        return doctorRepository.save(newDoctor);
    }

    public Doctor updateProfile(Long doctorId, DoctorProfileUpdate profileUpdate) {
        return doctorRepository.updateProfile(doctorId, profileUpdate);
    }

    public void registerHospital(Long doctorId, Long hospitalId) {
        doctorRepository.registerHospital(doctorId, hospitalId);
    }
}
