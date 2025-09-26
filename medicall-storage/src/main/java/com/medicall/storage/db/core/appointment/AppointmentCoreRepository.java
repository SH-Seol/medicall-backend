package com.medicall.storage.db.core.appointment;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentRepository;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentCoreRepository implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentCoreRepository(AppointmentJpaRepository appointmentJpaRepository,
                                     DoctorJpaRepository doctorJpaRepository,
                                     AppointmentRepository appointmentRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.appointmentRepository = appointmentRepository;
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

    public CursorPageResult<Appointment> findByPatientId(PatientAppointmentListCriteria criteria){
        List<AppointmentEntity> appointmentEntities;

        if(criteria.hasCursor()){
            appointmentEntities = appointmentJpaRepository.findTop16ByPatientIdAndIdLessThanOrderByIdDesc(
                    criteria.patientId(), criteria.cursorId());
        }
        else {
            appointmentEntities = appointmentJpaRepository.findTop16ByPatientIdOrderByIdDesc(criteria.patientId());
        }

        List<Appointment> appointments = appointmentEntities.stream()
                .map(AppointmentEntity::toDomainModel).toList();

        return CorePageUtils.buildCursorResult(appointments, criteria.size(), Appointment::id);
    }

    public List<Appointment> getAppointmentsByHospitalId(Long hospitalId){
        return null;
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId){
        return null;
    }

}
