package com.medicall.domain.appointment;

import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;

@Repository
public interface AppointmentRepository {
    Optional<Appointment> findById(Long appointmentId);
    void assignDoctorToAppointment(Appointment appointmentWithDoctor);
    CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria);
}
