package com.medicall.storage.db.core.hospital;

import com.medicall.domain.address.Address;
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
    public Hospital save(NewHospital newHospital, List<OperatingTime> operatingTimes){
        HospitalEntity savedHospitalEntity = new HospitalEntity(
                newHospital.name(),
                newHospital.imageUrl(),
                newHospital.oauthId(),
                newHospital.provider(),
                newHospital.email());

        return hospitalJpaRepository.save(savedHospitalEntity).toDomainModel();
    }

    public List<Appointment> findAppointmentsByHospitalId(Long hospitalId){
        List<AppointmentEntity> appointmentEntities = hospitalJpaRepository.findById(hospitalId).get().getAppointments();

        if(appointmentEntities == null){
            return List.of();
        }

        return appointmentEntities.stream().map(AppointmentEntity::toDomainModel).toList();
    }

    public boolean rejectAppointmentById(Long hospitalId, Long appointmentId){
        Optional<AppointmentEntity> appointmentOptional = appointmentJpaRepository.findById(appointmentId);
        if(appointmentOptional.isEmpty()){
            return false;
        }

        AppointmentEntity appointmentEntity = appointmentOptional.get();

        if(appointmentEntity.getHospital().getId().equals(hospitalId)){
            appointmentEntity.rejectAppointment();
        }
        return true;
    }

    public Long addDoctorOnAppointment(Long doctorId, Long appointmentId){
        AppointmentEntity appointmentEntity = appointmentJpaRepository.findById(appointmentId).orElseThrow();
        DoctorEntity doctorEntity = doctorJpaRepository.findById(doctorId).orElseThrow();
        appointmentEntity.addDoctor(doctorEntity);

        return appointmentId;
    }

    public void addOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes){
        HospitalEntity hospitalEntity = hospitalJpaRepository.findById(hospitalId).get();

        for(OperatingTime operatingTime : operatingTimes){
            OperatingTimeEntity operatingTimesEntity = new OperatingTimeEntity(
                    hospitalEntity,
                    operatingTime.dayOfWeek(),
                    operatingTime.openingTime(),
                    operatingTime.closingTime(),
                    operatingTime.breakStartTime(),
                    operatingTime.breakFinishTime()
            );

            hospitalEntity.addOperatingTime(operatingTimesEntity);
        }
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

    public Optional<Hospital> findByOAuthInfo(String oauthId, String provider){
        return hospitalJpaRepository.findByOauthIdAndOauthProvider(oauthId, provider).map(HospitalEntity::toDomainModel);
    }

    public void addAddress(Long hospitalId, Address address){
        Optional<HospitalEntity> hospitalEntity = hospitalJpaRepository.findById(hospitalId);

        hospitalEntity.ifPresent(hospital -> {
            AddressEntity addressEntity = new AddressEntity(
                    address.zoneCode(),
                    address.roadAddress(),
                    address.jibunAddress(),
                    address.detailAddress(),
                    address.buildingName(),
                    address.longitude(),
                    address.latitude());

            hospital.addAddress(addressEntity);
        });
    }

    public void addDepartments(Long hospitalId, List<Long> departments){
        List<DepartmentEntity> departmentEntities = departmentJpaRepository.findAllById(departments);
        Optional<HospitalEntity> hospitalEntity = hospitalJpaRepository.findById(hospitalId);

        hospitalEntity.ifPresent(hospital -> hospital.addDepartments(departmentEntities));
    }

}
