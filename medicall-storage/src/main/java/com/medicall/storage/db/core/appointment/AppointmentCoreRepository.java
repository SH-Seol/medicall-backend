package com.medicall.storage.db.core.appointment;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentRepository;
import com.medicall.domain.appointment.NewAppointment;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.storage.db.core.address.AddressEntity;
import com.medicall.storage.db.core.address.AddressJpaRepository;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import com.medicall.storage.db.core.hospital.HospitalEntity;
import com.medicall.storage.db.core.hospital.HospitalJpaRepository;
import com.medicall.storage.db.core.patient.PatientEntity;
import com.medicall.storage.db.core.patient.PatientJpaRepository;
import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentCoreRepository implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    public AppointmentCoreRepository(AppointmentJpaRepository appointmentJpaRepository,
                                     DoctorJpaRepository doctorJpaRepository,
                                     AppointmentRepository appointmentRepository,
                                     PatientJpaRepository patientJpaRepository,
                                     HospitalJpaRepository hospitalJpaRepository,
                                     AddressJpaRepository addressJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.hospitalJpaRepository = hospitalJpaRepository;
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

    public Appointment create(Long patientId, NewAppointment newAppointment){
        PatientEntity patientEntity = patientJpaRepository.getReferenceById(patientId);
        DoctorEntity doctorEntity = doctorJpaRepository.getReferenceById(newAppointment.doctorId());
        HospitalEntity hospitalEntity = hospitalJpaRepository.getReferenceById(newAppointment.hospitalId());

        Address address = newAppointment.address();

        AddressEntity addressEntity = new AddressEntity(address.zoneCode(), address.roadAddress(),
                address.jibunAddress(), address.detailAddress(), address.buildingName(), address.longitude(),
                address.latitude());

        AppointmentEntity appointmentEntity = new AppointmentEntity(patientEntity, doctorEntity, hospitalEntity,
                newAppointment.symptom(), addressEntity, newAppointment.reservationTime());

        AppointmentEntity savedAppointment = appointmentJpaRepository.save(appointmentEntity);

        return savedAppointment.toDomainModel();
    }

    public boolean existsByDoctorIdAndReservationTime(Long doctorId, LocalDateTime reservationTime){
        return appointmentJpaRepository.existsByDoctorIdAndReservationTime(doctorId, reservationTime);
    }

    public boolean existsByPatientIdAndReservationTime(Long patientId, LocalDateTime reservationTime){
        return appointmentJpaRepository.existsByPatientIdAndReservationTime(patientId, reservationTime);
    }

    public List<Appointment> getAppointmentsByHospitalId(Long hospitalId){
        return null;
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId){
        return null;
    }

}
