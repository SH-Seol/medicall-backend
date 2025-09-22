package com.medicall.domain.hospital;

import java.util.List;
import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class HospitalWriter {

    private final HospitalRepository hospitalRepository;

    public HospitalWriter(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Hospital create(NewHospital newHospital) {
        return hospitalRepository.save(newHospital);
    }

    public void rejectAppointment(Long hospitalId, Long appointmentId) {
        boolean result = hospitalRepository.rejectAppointmentById(hospitalId, appointmentId);
    }

    public boolean addDoctorOnAppointment(Long doctorId, Long appointmentId) {
        boolean success = hospitalRepository.addDoctorOnAppointment(doctorId, appointmentId);
        if(!success){
            throw new CoreException(CoreErrorType.APPOINTMENT_ADD_DOCTOR_FAILED);
        }
        return success;
    }

    public boolean updaterOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes) {
        boolean success = hospitalRepository.updateOperatingTimes(hospitalId, operatingTimes);
        if(!success){
            throw new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND);
        }
        return success;
    }
}
