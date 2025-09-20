package com.medicall.domain.doctor;

import org.springframework.stereotype.Component;

@Component
public class DoctorWriter {

    private final DoctorRepository doctorRepository;

    public DoctorWriter(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(Doctor newDoctor) {
        return doctorRepository.save(newDoctor);
    }
}
