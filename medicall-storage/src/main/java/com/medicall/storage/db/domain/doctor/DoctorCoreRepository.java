package com.medicall.storage.db.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorRepository;
import com.medicall.domain.doctor.dto.DoctorProfileUpdate;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.department.DepartmentEntity;
import com.medicall.storage.db.domain.department.DepartmentJpaRepository;
import com.medicall.storage.db.domain.department.SpecialtyEntity;
import com.medicall.storage.db.domain.department.SpecialtyJpaRepository;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.hospital.HospitalJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorCoreRepository implements DoctorRepository {

    private final DoctorJpaRepository doctorJpaRepository;
    private final DepartmentJpaRepository departmentJpaRepository;
    private final SpecialtyJpaRepository specialtyJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    public DoctorCoreRepository(DoctorJpaRepository doctorJpaRepository,
                                DepartmentJpaRepository departmentJpaRepository,
                                SpecialtyJpaRepository specialtyJpaRepository,
                                HospitalJpaRepository hospitalJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.departmentJpaRepository = departmentJpaRepository;
        this.specialtyJpaRepository = specialtyJpaRepository;
        this.hospitalJpaRepository = hospitalJpaRepository;
    }

    public Doctor save(Doctor newDoctor) {
        // OAuth 최초 가입 시점에는 진료과가 정해지지 않은 상태로 저장된다. (이후 내 정보 수정에서 등록)
        DepartmentEntity department = newDoctor.department() == null ? null
                : departmentJpaRepository.findById(newDoctor.department().id())
                        .orElseThrow(() -> new CoreException(CoreErrorType.DEPARTMENT_NOT_FOUND));
        DoctorEntity savedDoctor = doctorJpaRepository.save(new DoctorEntity(newDoctor.name(), newDoctor.imageUrl(),
                newDoctor.introduction(), department, newDoctor.oauthId(), newDoctor.provider()));

        return savedDoctor.toDomainModel();
    }

    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        Optional<DoctorEntity> doctorEntity = doctorJpaRepository.findById(doctor.id());

        if(doctorEntity.isEmpty()){
            return List.of();
        }

        return doctorEntity.get().getAppointments().stream()
                .map(AppointmentEntity::toDomainModel)
                .toList();
    }

    public Optional<Doctor> findById(Long doctorId) {
        return doctorJpaRepository.findById(doctorId).map(DoctorEntity::toDomainModel);
    }

    public boolean isDoctorBelongsToHospital(Long doctorId) {
        DoctorEntity doctorEntity = doctorJpaRepository.findByIdWithOptionalHospital(doctorId);

        return doctorEntity.getHospital() != null;
    }

    public Optional<Doctor> findByOAuthInfo(String oauthId, String provider){
        return doctorJpaRepository.findByOauthIdAndOauthProvider(oauthId, provider).map(DoctorEntity::toDomainModel);
    }

    public Doctor updateProfile(Long doctorId, DoctorProfileUpdate profileUpdate) {
        DoctorEntity doctorEntity = doctorJpaRepository.findById(doctorId)
                .orElseThrow(() -> new CoreException(CoreErrorType.DOCTOR_NOT_FOUND));

        DepartmentEntity department = profileUpdate.departmentId() == null ? null
                : departmentJpaRepository.findById(profileUpdate.departmentId())
                        .orElseThrow(() -> new CoreException(CoreErrorType.DEPARTMENT_NOT_FOUND));

        SpecialtyEntity specialty = profileUpdate.specialtyId() == null ? null
                : specialtyJpaRepository.findById(profileUpdate.specialtyId())
                        .orElseThrow(() -> new CoreException(CoreErrorType.SPECIALTY_NOT_FOUND));

        doctorEntity.updateProfile(
                profileUpdate.name(),
                profileUpdate.introduction(),
                profileUpdate.imageUrl(),
                department,
                specialty
        );

        return doctorEntity.toDomainModel();
    }

    public void registerHospital(Long doctorId, Long hospitalId) {
        DoctorEntity doctorEntity = doctorJpaRepository.findById(doctorId)
                .orElseThrow(() -> new CoreException(CoreErrorType.DOCTOR_NOT_FOUND));
        HospitalEntity hospitalEntity = hospitalJpaRepository.findById(hospitalId)
                .orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        doctorEntity.registerHospital(hospitalEntity);
    }

    public List<Doctor> findAllByHospitalId(Long hospitalId) {
        return doctorJpaRepository.findAllByHospitalId(hospitalId).stream()
                .map(DoctorEntity::toDomainModel)
                .toList();
    }

    public boolean isDoctorExist(Long doctorId) {
        return doctorJpaRepository.existsById(doctorId);
    }
}
