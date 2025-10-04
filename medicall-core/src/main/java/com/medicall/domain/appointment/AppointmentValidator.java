package com.medicall.domain.appointment;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.medicall.domain.hospital.HospitalValidator;
import com.medicall.domain.doctor.DoctorValidator;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class AppointmentValidator {

    private final DoctorValidator doctorValidator;
    private final HospitalValidator hospitalValidator;
    private final AppointmentRepository appointmentRepository;

    AppointmentValidator (DoctorValidator doctorValidator, HospitalValidator hospitalValidator,
                          AppointmentRepository appointmentRepository) {
        this.doctorValidator = doctorValidator;
        this.hospitalValidator = hospitalValidator;
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * 해당 환자의 예약이 맞는지 검증
     */
    public void validatePatientAccess(Appointment appointment, Long patientId) {
        if(!appointment.patient().id().equals(patientId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }

    /**
     * 해당 의사의 예약이 맞는지 검증
     */
    public void validateDoctorAccess(Appointment appointment, Long doctorId) {
        if(!appointment.doctor().id().equals(doctorId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }

    /**
     * 해당 병원의 예약이 맞는지 검증
     */
    public void validateHospitalAccess(Appointment appointment, Long hospitalId) {
        if(!appointment.hospital().id().equals(hospitalId)) {
            throw new CoreException(CoreErrorType.APPOINTMENT_EXCESS_REJECTED);
        }
    }

    /**
     * 얘역 생성 종합 검증
     */
    public void validateAppointmentCreation(Long patientId, NewAppointment newAppointment) {
        doctorValidator.validateDoctor(newAppointment.doctorId());
        hospitalValidator.validateHospital(newAppointment.hospitalId());

        validatePatientDuplicateReservation(patientId, newAppointment.reservationTime());
        validateDoctorAvailability(newAppointment.doctorId(), newAppointment.reservationTime());
        validateReservationTime(newAppointment.reservationTime());
    }

    /**
     * 환자가 같은 시간에 다른 예약을 했는지 검증
     */
    public void validatePatientDuplicateReservation(Long patientId, LocalDateTime reservationTime) {
        boolean result = appointmentRepository.existsByPatientIdAndReservationTime(patientId, reservationTime);
        if(!result){
            throw new CoreException(CoreErrorType.APPOINTMENT_ALREADY_EXIST_FOR_PATIENT);
        }
    }

    /**
     * 의사가 요청 시간에 이미 진료 예약이 있는지 검증
     */
    public void validateDoctorAvailability(Long doctorId, LocalDateTime reservationTime) {
        boolean result = appointmentRepository.existsByDoctorIdAndReservationTime(doctorId, reservationTime);
        if(!result){
            throw new CoreException(CoreErrorType.APPOINTMENT_ALREADY_EXIST_FOR_DOCTOR);
        }
    }

    /**
     * 예약 시간이 지금보다 전은 아닌지 검증
     */
    private void validateReservationTime(LocalDateTime reservationTime) {
        if(reservationTime.isBefore(LocalDateTime.now())) {
            throw new CoreException(CoreErrorType.APPOINTMENT_RESERVATION_TIME_IS_PAST);
        }
    }
}
