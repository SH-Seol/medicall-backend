package com.medicall.storage.db.core.appointment;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentRepository;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import java.util.List;
import java.util.Optional;

public class AppointmentCoreRepository implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;

    public AppointmentCoreRepository(AppointmentJpaRepository appointmentJpaRepository,
                                     DoctorJpaRepository doctorJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    public Optional<Appointment> findById(Long appointmentId){
        return appointmentJpaRepository.findById(appointmentId).map(AppointmentEntity::toDomainModel);
    }

    public void assignDoctorToAppointment(Appointment appointment){
        DoctorEntity doctorEntity = doctorJpaRepository.getReferenceById(appointment.doctor()
                .id());
        AppointmentEntity appointmentEntity = appointmentJpaRepository.getReferenceById(appointment.id());

        appointmentEntity.addDoctor(doctorEntity);
    }

    public List<Appointment> getAppointmentsByHospitalId(Long hospitalId){
        return null;
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId){
        return null;
    }
}
