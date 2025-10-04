package com.medicall.storage.db.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorRepository;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.department.DepartmentEntity;
import com.medicall.storage.db.domain.department.DepartmentJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorCoreRepository implements DoctorRepository {

    private final DoctorJpaRepository doctorJpaRepository;
    private final DepartmentJpaRepository departmentJpaRepository;

    public DoctorCoreRepository(DoctorJpaRepository doctorJpaRepository, DepartmentJpaRepository departmentJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.departmentJpaRepository = departmentJpaRepository;
    }

    public Doctor save(Doctor newDoctor) {
        DepartmentEntity department = departmentJpaRepository.getReferenceById(newDoctor.department()
                .id());
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

    public boolean isDoctorExist(Long doctorId) {
        return doctorJpaRepository.existsById(doctorId);
    }
}
