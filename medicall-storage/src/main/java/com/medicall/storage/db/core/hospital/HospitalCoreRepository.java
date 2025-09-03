package com.medicall.storage.db.core.hospital;


import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.HospitalRepository;
import com.medicall.domain.hospital.NewHospital;
import com.medicall.domain.hospital.OperatingTime;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.core.address.AddressEntity;
import com.medicall.storage.db.core.appointment.AppointmentEntity;
import com.medicall.storage.db.core.appointment.AppointmentJpaRepository;
import com.medicall.storage.db.core.department.DepartmentEntity;
import com.medicall.storage.db.core.department.DepartmentJpaRepository;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalCoreRepository implements HospitalRepository {

    private final HospitalJpaRepository hospitalJpaRepository;
    private final DepartmentJpaRepository departmentJpaRepository;
    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final JPAQueryFactory queryFactory;

    public HospitalCoreRepository(HospitalJpaRepository hospitalJpaRepository,
                                  DepartmentJpaRepository departmentJpaRepository,
                                  AppointmentJpaRepository appointmentJpaRepository,
                                  DoctorJpaRepository doctorJpaRepository,
                                  JPAQueryFactory queryFactory) {
        this.hospitalJpaRepository = hospitalJpaRepository;
        this.departmentJpaRepository = departmentJpaRepository;
        this.appointmentJpaRepository = appointmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.queryFactory = queryFactory;
    }
    public Long save(NewHospital newHospital, List<OperatingTime> operatingTimes){
        AddressEntity addressEntity = new AddressEntity(newHospital.address().zoneCode(),
                newHospital.address().roadAddress(),
                newHospital.address().jibunAddress(),
                newHospital.address().detailAddress(),
                newHospital.address().buildingName(),
                newHospital.address().longitude(),
                newHospital.address().latitude());

        List<DepartmentEntity> departmentEntities = departmentJpaRepository.findAllById(newHospital.departments());

        HospitalEntity savedHospitalEntity = new HospitalEntity(
                newHospital.name(),
                newHospital.telephoneNumber(),
                addressEntity,
                newHospital.imageUrl());

        savedHospitalEntity.addDepartments(departmentEntities);

        for(OperatingTime operatingTime : operatingTimes){
            OperatingTimeEntity operatingTimesEntity = new OperatingTimeEntity(
                    savedHospitalEntity,
                    operatingTime.dayOfWeek(),
                    operatingTime.openingTime(),
                    operatingTime.closingTime(),
                    operatingTime.breakStartTime(),
                    operatingTime.breakFinishTime()
                    );

            savedHospitalEntity.addOperatingTime(operatingTimesEntity);
        }

        return hospitalJpaRepository.save(savedHospitalEntity).getId();
    }

    public List<Appointment> findAppointmentsByHospitalId(Long hospitalId){
        List<AppointmentEntity> appointmentEntities = hospitalJpaRepository.findById(hospitalId).get().getAppointments();

        if(appointmentEntities == null){
            return List.of();
        }

        return appointmentEntities.stream().map(AppointmentEntity::toDomainModel).toList();
    }

    public void rejectAppointmentById(Long hospitalId, Long appointmentId){
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findById(appointmentId).orElseThrow();
        if(appointmentEntity.getHospital().getId().equals(hospitalId)){
            appointmentEntity.rejectAppointment();
        }
    }

    public Long addDoctorOnAppointment(Long doctorId, Long appointmentId){
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findById(appointmentId).orElseThrow();
        DoctorEntity doctorEntity = doctorJpaRepository.findById(doctorId).orElseThrow();
        appointmentEntity.addDoctor(doctorEntity);

        return appointmentId;
    }

    public void updateOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes){
        HospitalEntity hospitalEntity = findWithOperationTimesById(hospitalId).orElseThrow(
                () -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        Map<DayOfWeek, OperatingTimeEntity> existingTimesMap = hospitalEntity.getOperatingTimes()
                .stream().collect(Collectors.toMap(OperatingTimeEntity::getDayOfWeek, operatingTimeEntity -> operatingTimeEntity));

        Map<DayOfWeek, OperatingTime> newTimesMap = operatingTimes.stream()
                .collect(Collectors.toMap(OperatingTime::dayOfWeek, operatingTimeEntity -> operatingTimeEntity));

        newTimesMap.forEach((dayOfWeek, newTime) -> {
            if(existingTimesMap.containsKey(dayOfWeek)){
                existingTimesMap.get(dayOfWeek).updateFromDomainModel(newTime);
            }
            else{
                OperatingTimeEntity operatingTimeEntity = new OperatingTimeEntity(
                        hospitalEntity,
                        dayOfWeek,
                        newTime.openingTime(),
                        newTime.closingTime(),
                        newTime.breakStartTime(),
                        newTime.breakFinishTime()
                );
                hospitalEntity.addOperatingTime(operatingTimeEntity);
            }
        });

        List<DayOfWeek> toRemove = existingTimesMap.keySet().stream()
                .filter(dayOfWeek -> !newTimesMap.containsKey(dayOfWeek))
                .toList();

        hospitalEntity.getOperatingTimes().removeIf(entity -> toRemove.contains(entity.getDayOfWeek()));

        hospitalJpaRepository.save(hospitalEntity);
    }

    public Optional<Hospital> findById(Long hospitalId){
        return hospitalJpaRepository.findById(hospitalId).map(HospitalEntity::toDomainModel);
    }

    public List<Hospital> findAllByKeyword(String keyword){
        return hospitalJpaRepository.findByNameIgnoreCase(keyword.trim())
                .stream()
                .map(HospitalEntity::toDomainModel)
                .toList();
    }

    private Optional<HospitalEntity> findWithOperationTimesById(Long hospitalId){
        QHospitalEntity hospitalEntity = QHospitalEntity.hospitalEntity;
        QOperatingTimeEntity operatingTimeEntity = QOperatingTimeEntity.operatingTimeEntity;

        HospitalEntity result = queryFactory
                .selectFrom(hospitalEntity)
                .leftJoin(hospitalEntity.operatingTimes, operatingTimeEntity).fetchJoin()
                .where(hospitalEntity.id.eq(hospitalId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
