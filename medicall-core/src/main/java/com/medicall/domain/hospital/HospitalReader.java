package com.medicall.domain.hospital;

import com.medicall.domain.appointment.Appointment;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HospitalReader {

    private final HospitalRepository hospitalRepository;

    public HospitalReader(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public List<Appointment> getAppointments(Long hospitalId) {
        return hospitalRepository.findAppointmentsByHospitalId(hospitalId);
    }

    public Hospital findById(Long hospitalId) {
        return hospitalRepository.findById(hospitalId).orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));
    }

    public List<Hospital> findAllByKeyword(String keyword) {
        return hospitalRepository.findAllByKeyword(keyword);
    }
}
