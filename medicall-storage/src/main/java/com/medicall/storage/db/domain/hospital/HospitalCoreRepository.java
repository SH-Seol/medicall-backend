package com.medicall.storage.db.domain.hospital;

import com.medicall.domain.address.Address;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.hospital.BoundingBox;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.HospitalRepository;
import com.medicall.domain.hospital.NewHospital;
import com.medicall.domain.hospital.OperatingTime;
import com.medicall.domain.hospital.dto.HospitalProfileUpdate;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.appointment.AppointmentJpaRepository;
import com.medicall.storage.db.domain.department.DepartmentEntity;
import com.medicall.storage.db.domain.department.DepartmentJpaRepository;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
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
    @Transactional
    public Hospital save(NewHospital newHospital){
        HospitalEntity savedHospitalEntity = new HospitalEntity(
                newHospital.name(),
                newHospital.imageUrl(),
                newHospital.oauthId(),
                newHospital.provider(),
                newHospital.email());

        return hospitalJpaRepository.save(savedHospitalEntity).toDomainModel();
    }

    public Optional<List<Appointment>> findAppointmentsByHospitalId(Long hospitalId){
        return hospitalJpaRepository.findById(hospitalId).map(hospital -> {
            List<AppointmentEntity> appointmentEntities = hospital.getAppointments();

            if(appointmentEntities.isEmpty() || appointmentEntities == null){
                return List.of();
            }

            return appointmentEntities.stream().map(AppointmentEntity::toDomainModel).toList();
        });
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

    public boolean addDoctorOnAppointment(Long doctorId, Long appointmentId){
        Optional<AppointmentEntity> appointmentOptional = appointmentJpaRepository.findById(appointmentId);
        Optional<DoctorEntity> doctorOptional = doctorJpaRepository.findById(doctorId);

        if(appointmentOptional.isEmpty() || doctorOptional.isEmpty()){
            return false;
        }

        appointmentOptional.get().addDoctor(doctorOptional.get());
        return true;
    }

    public boolean updateOperatingTimes(Long hospitalId, List<OperatingTime> operatingTimes){
        Optional<HospitalEntity> hospitalOptional = findWithOperationTimesById(hospitalId);

        if(hospitalOptional.isEmpty()){
            return false;
        }

        HospitalEntity hospitalEntity = hospitalOptional.get();

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
                operatingTimeEntity.updateFromDomainModel(newTime);
                hospitalEntity.addOperatingTime(operatingTimeEntity);
            }
        });

        List<DayOfWeek> toRemove = existingTimesMap.keySet().stream()
                .filter(dayOfWeek -> !newTimesMap.containsKey(dayOfWeek))
                .toList();

        hospitalEntity.getOperatingTimes().removeIf(entity -> toRemove.contains(entity.getDayOfWeek()));

        return true;
    }

    public Optional<Hospital> findById(Long hospitalId){
        return hospitalJpaRepository.findByIdWithDetails(hospitalId).map(HospitalEntity::toDomainModel);
    }

    /**
     * 검색 조건에 맞는 모든 병원 조회
     * (keyword, departmentId 둘 중 하나는 null)
     */
    public List<Hospital> findAllWithinBoundingBox(BoundingBox boundingBox, String keyword, Long departmentId, Long cursorId, int size){
        QHospitalEntity hospital = QHospitalEntity.hospitalEntity;

        return queryFactory
                .selectFrom(hospital)
                .distinct()
                .where(
                        withinBox(boundingBox),
                        keywordContains(keyword),
                        cursorIdGt(cursorId),
                        hasDepartment(departmentId)
                ).orderBy(hospital.id.asc())
                .limit(size)
                .fetch()
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

    public void updateAddress(Long hospitalId, Address address){
        HospitalEntity hospital = hospitalJpaRepository.findById(hospitalId)
                .orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        AddressEntity addressEntity = new AddressEntity(
                address.zoneCode(),
                address.roadAddress(),
                address.jibunAddress(),
                address.detailAddress(),
                address.buildingName(),
                address.longitude(),
                address.latitude());

        // 병원 주소는 1건만 유지한다. (orphanRemoval로 기존 주소는 삭제)
        hospital.addAddress(addressEntity);
    }

    public void updateDepartments(Long hospitalId, List<Long> departmentIds){
        HospitalEntity hospital = hospitalJpaRepository.findById(hospitalId)
                .orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        List<DepartmentEntity> departmentEntities = departmentJpaRepository.findAllById(departmentIds);
        if(departmentEntities.size() != departmentIds.stream().distinct().count()){
            throw new CoreException(CoreErrorType.DEPARTMENT_NOT_FOUND);
        }

        hospital.replaceDepartments(departmentEntities);
    }

    public Hospital updateProfile(Long hospitalId, HospitalProfileUpdate profileUpdate){
        HospitalEntity hospitalEntity = hospitalJpaRepository.findById(hospitalId)
                .orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        hospitalEntity.updateProfile(
                profileUpdate.name(),
                profileUpdate.telephoneNumber(),
                profileUpdate.imageUrl()
        );

        return hospitalEntity.toDomainModel();
    }

    public boolean isHospitalExist(Long hospitalId){
        return hospitalJpaRepository.existsById(hospitalId);
    }

    /**
     * 병원 진료 과목에 departmentId에 해당하는 진료과목이 존재하는지 조회
     */
    public BooleanExpression hasDepartment(Long departmentId){
        if(departmentId == null){
            return null;
        }
        QHospitalEntity hospital = QHospitalEntity.hospitalEntity;
        return hospital.departments.any().department.id.eq(departmentId);
    }

    /**
     * BoundingBox 내에 존재하는지 검증
     */
    private BooleanExpression withinBox(BoundingBox boundingBox){
        QHospitalEntity hospital = QHospitalEntity.hospitalEntity;
        return hospital.address.latitude.between(boundingBox.minLat(), boundingBox.maxLat())
                .and(hospital.address.longitude.between(boundingBox.minLon(), boundingBox.maxLon()));
    }

    /**
     * 검색어를 포함하는지 검증
     */
    private BooleanExpression keywordContains(String keyword){
        QHospitalEntity hospital = QHospitalEntity.hospitalEntity;

        if(keyword == null || keyword.isEmpty()){
            return null;
        }
        return hospital.name.containsIgnoreCase(keyword);
    }

    /**
     * 커서 ID 이후의 데이터만 조회하도록 조건 생성
     * (cursorId가 null이면 조건 무시)
     */
    private BooleanExpression cursorIdGt(Long cursorId){
        QHospitalEntity hospital = QHospitalEntity.hospitalEntity;

        return cursorId != null ? hospital.id.gt(cursorId) : null;
    }
}
