package com.medicall.domain.appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.support.CursorPageResult;

@Repository
public interface AppointmentRepository {
    Optional<Appointment> findById(Long appointmentId);
    boolean assignDoctorToAppointment(Long appointmentId, Long hospitalId, Long doctorId);
    CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria);
    Appointment create(Long patientId, NewAppointment newAppointment);
    boolean existsByDoctorIdAndReservationTime(Long doctorId, LocalDateTime reservationTime);
    boolean existsByPatientIdAndReservationTime(Long patientId, LocalDateTime reservationTime);
    List<Appointment> findAllByDoctorId(Long doctorId, Long cursorId, int size);
    List<LocalDateTime> findActiveReservationTimes(Long doctorId, LocalDateTime from, LocalDateTime to);
    List<Appointment> findAllByHospitalId(Long hospitalId, Long cursorId, int size);
    boolean acceptAppointment(Long appointmentId, Long hospitalId);
    boolean cancelAppointment(Long appointmentId, Long patientId);
    boolean rejectAppointment(Long appointmentId, Long hospitalId);
    boolean updateStatusByDoctor(Long appointmentId, Long doctorId, AppointmentStatus expected, AppointmentStatus next);
}
