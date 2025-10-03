package com.medicall.domain.hospital;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;

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
    List<Hospital> findAllByKeywordWithinBoundingBox(BoundingBox boundingBox, String keyword, Long cursorId, int size);
    Optional<Hospital> findByOAuthInfo(String oauthId, String provider);
    boolean addOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes);
    boolean addAddress(Long hospitalId, Address address);
    boolean addDepartments(Long hospitalId, List<Long> departments);
    boolean isHospitalExist(Long hospitalId);
}
