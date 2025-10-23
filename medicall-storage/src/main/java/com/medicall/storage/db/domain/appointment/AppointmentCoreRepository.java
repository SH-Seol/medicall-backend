package com.medicall.storage.db.domain.appointment;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentRepository;
import com.medicall.domain.appointment.NewAppointment;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.hospital.HospitalJpaRepository;
import com.medicall.storage.db.domain.patient.PatientEntity;
import com.medicall.storage.db.domain.patient.PatientJpaRepository;
import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AppointmentCoreRepository implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;
    private final JPAQueryFactory queryFactory;

    public AppointmentCoreRepository(AppointmentJpaRepository appointmentJpaRepository,
                                     DoctorJpaRepository doctorJpaRepository,
                                     PatientJpaRepository patientJpaRepository,
                                     HospitalJpaRepository hospitalJpaRepository,
                                     JPAQueryFactory queryFactory) {
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.hospitalJpaRepository = hospitalJpaRepository;
        this.queryFactory = queryFactory;
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

    public List<Appointment> findAllByDoctorId(Long doctorId, Long cursorId, int size){
        QAppointmentEntity appointmentEntity = QAppointmentEntity.appointmentEntity;

        return queryFactory.selectFrom(appointmentEntity)
                .where(appointmentEntity.doctor.id.eq(doctorId),
                        cursorIdGt(cursorId))
                .orderBy(appointmentEntity.id.desc())
                .limit(size)
                .fetch()
                .stream()
                .map(AppointmentEntity::toDomainModel)
                .toList();
    }

    private BooleanExpression cursorIdGt(Long cursorId){
        QAppointmentEntity appointment = QAppointmentEntity.appointmentEntity;

        return cursorId != null ? appointment.id.gt(cursorId) : null;
    }
}
