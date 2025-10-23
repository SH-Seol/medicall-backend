package com.medicall.domain.appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;

@Repository
public interface AppointmentRepository {
    Optional<Appointment> findById(Long appointmentId);
    void assignDoctorToAppointment(Appointment appointmentWithDoctor);
    CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria);
    Appointment create(Long patientId, NewAppointment newAppointment);
    boolean existsByDoctorIdAndReservationTime(Long doctorId, LocalDateTime reservationTime);
    boolean existsByPatientIdAndReservationTime(Long patientId, LocalDateTime reservationTime);
    List<Appointment> findAllByDoctorId(Long doctorId, Long cursorId, int size);
    List<Appointment> findAllByHospitalId(Long hospitalId, Long cursorId, int size);
}
