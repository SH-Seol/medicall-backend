package com.medicall.domain.hospital;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.hospital.dto.HospitalProfileUpdate;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository {
    Hospital save(NewHospital newHospital);
    Optional<List<Appointment>> findAppointmentsByHospitalId(Long id);
    boolean rejectAppointmentById(Long hospitalId, Long appointmentId);
    boolean addDoctorOnAppointment(Long doctorId, Long appointmentId);
    boolean updateOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes);
    Optional<Hospital> findById(Long hospitalId);
    List<Hospital> findAllWithinBoundingBox(BoundingBox boundingBox, String keyword, Long departmentId, Long cursorId, int size);
    Optional<Hospital> findByOAuthInfo(String oauthId, String provider);
    void updateAddress(Long hospitalId, Address address);
    void updateDepartments(Long hospitalId, List<Long> departmentIds);
    boolean isHospitalExist(Long hospitalId);
    Hospital updateProfile(Long hospitalId, HospitalProfileUpdate profileUpdate);
}
