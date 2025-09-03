package com.medicall.domain.hospital;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class HospitalWriter {

    private final HospitalRepository hospitalRepository;

    public HospitalWriter(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Long create(NewHospital newHospital , List<OperatingTime> operatingTimes) {
        return hospitalRepository.save(newHospital, operatingTimes);
    }

    public void rejectAppointment(Long hospitalId, Long appointmentId) {
        hospitalRepository.rejectAppointmentById(hospitalId, appointmentId);
    }

    public Long addDoctorOnAppointment(Long doctorId, Long appointmentId) {
        return hospitalRepository.addDoctorOnAppointment(doctorId, appointmentId);
    }

    public void updaterOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes) {
        hospitalRepository.updateOperatingTimes(hospitalId, operatingTimes);
    }
}
