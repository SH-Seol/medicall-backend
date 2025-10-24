package com.medicall.storage.db.domain.treatment;

import static com.medicall.storage.db.domain.treatment.QTreatmentEntity.treatmentEntity;

import com.medicall.domain.treatment.NewTreatment;
import com.medicall.domain.treatment.Treatment;
import com.medicall.domain.treatment.TreatmentRepository;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.medicall.storage.db.domain.patient.PatientEntity;
import com.medicall.storage.db.domain.patient.PatientJpaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
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

    public List<Treatment> getTreatmentsByPatientId(Long patientId, Long doctorId, Long cursorId, int size) {
        List<TreatmentEntity> entities = queryFactory
                .selectFrom(treatmentEntity)
                .where(treatmentEntity.patient.id.eq(patientId),
                        treatmentEntity.doctor.id.eq(doctorId),
                        cursorIdGt(cursorId)
                ).orderBy(treatmentEntity.id.desc())
                .limit(size)
                .fetch();

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

    public List<Treatment> findAllByPatientId(Long patientId, Long cursorId, int size){
        List<TreatmentEntity> treatmentEntities = treatmentJpaRepository.findAllByPatientId(patientId);
        return treatmentEntities.stream().map(TreatmentEntity::toDomainModel).toList();
    }

    private BooleanExpression cursorIdGt(Long cursorId){
        QTreatmentEntity treatment = QTreatmentEntity.treatmentEntity;

        return cursorId != null ? treatment.id.gt(cursorId) : null;
    }
}
