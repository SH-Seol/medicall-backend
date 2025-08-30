package com.medicall.storage.db.core.treatment;

import static com.medicall.storage.db.core.treatment.QTreatmentEntity.treatmentEntity;

import com.medicall.domain.treatment.NewTreatment;
import com.medicall.domain.treatment.Treatment;
import com.medicall.domain.treatment.TreatmentRepository;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import com.medicall.storage.db.core.patient.PatientEntity;
import com.medicall.storage.db.core.patient.PatientJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;

public class TreatmentCoreRepository implements TreatmentRepository {

    private final TreatmentJpaRepository treatmentJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final JPAQueryFactory queryFactory;

    public TreatmentCoreRepository(TreatmentJpaRepository treatmentJpaRepository,
                                   DoctorJpaRepository doctorJpaRepository,
                                   PatientJpaRepository patientJpaRepository,
                                   JPAQueryFactory queryFactory) {
        this.treatmentJpaRepository = treatmentJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.queryFactory = queryFactory;
    }

    public List<Treatment> getTreatmentsByPatientId(Long patientId, Long doctorId) {
        List<TreatmentEntity> entities = queryFactory
                .selectFrom(treatmentEntity)
                .where(treatmentEntity.patient.id.eq(patientId),
                        treatmentEntity.doctor.id.eq(doctorId)
                ).fetch();

        return entities.stream().map(TreatmentEntity::toDomainModel).toList();
    }

    public Long save(NewTreatment treatment) {
        PatientEntity patientEntity = patientJpaRepository.getReferenceById(treatment.patientId());
        DoctorEntity doctorEntity = doctorJpaRepository.getReferenceById(treatment.doctorId());

        TreatmentEntity treatmentEntity = new TreatmentEntity(
                patientEntity,
                doctorEntity,
                treatment.symptoms(),
                treatment.treatment(),
                treatment.detailedTreatment());

        return treatmentJpaRepository.save(treatmentEntity).getId();
    }

    public Optional<Treatment> findById(Long treatmentId){
        return treatmentJpaRepository.findById(treatmentId).map(TreatmentEntity::toDomainModel);
    }
}
