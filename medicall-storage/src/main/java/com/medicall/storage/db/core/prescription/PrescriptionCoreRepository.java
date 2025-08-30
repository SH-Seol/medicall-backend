package com.medicall.storage.db.core.prescription;

import com.medicall.domain.prescription.NewPrescription;
import com.medicall.domain.prescription.Prescription;
import com.medicall.domain.prescription.PrescriptionMedicine;
import com.medicall.domain.prescription.PrescriptionRepository;
import com.medicall.storage.db.core.doctor.DoctorEntity;
import com.medicall.storage.db.core.doctor.DoctorJpaRepository;
import com.medicall.storage.db.core.hospital.HospitalEntity;
import com.medicall.storage.db.core.medicine.MedicineEntity;
import com.medicall.storage.db.core.medicine.MedicineJpaRepository;
import com.medicall.storage.db.core.patient.PatientEntity;
import com.medicall.storage.db.core.patient.PatientJpaRepository;
import com.medicall.storage.db.core.treatment.TreatmentEntity;
import com.medicall.storage.db.core.treatment.TreatmentJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrescriptionCoreRepository implements PrescriptionRepository {

    private final PrescriptionJpaRepository prescriptionJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;
    private final MedicineJpaRepository medicineJpaRepository;
    private final TreatmentJpaRepository treatmentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    public PrescriptionCoreRepository(PrescriptionJpaRepository prescriptionJpaRepository,
                                      PatientJpaRepository patientJpaRepository,
                                      DoctorJpaRepository doctorJpaRepository,
                                      MedicineJpaRepository medicineJpaRepository,
                                      TreatmentJpaRepository treatmentJpaRepository,
                                      JPAQueryFactory jpaQueryFactory) {
        this.prescriptionJpaRepository = prescriptionJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
        this.medicineJpaRepository = medicineJpaRepository;
        this.treatmentJpaRepository = treatmentJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Prescription> getPrescriptionByPatientIdAndDoctorId(Long patientId, Long doctorId){
        QPrescriptionEntity prescriptionEntity = QPrescriptionEntity.prescriptionEntity;
        QPrescriptionMedicineEntity prescriptionMedicineEntity = QPrescriptionMedicineEntity.prescriptionMedicineEntity;

        List<PrescriptionEntity> prescriptionEntityList = jpaQueryFactory
                .selectFrom(prescriptionEntity)
                .leftJoin(prescriptionEntity.patient).fetchJoin()
                .leftJoin(prescriptionEntity.doctor).fetchJoin()
                .leftJoin(prescriptionEntity.hospital).fetchJoin()
                .leftJoin(prescriptionEntity.PrescriptionMedicineList, prescriptionMedicineEntity).fetchJoin()
                .leftJoin(prescriptionMedicineEntity.medicine).fetchJoin()
                .where(
                        prescriptionEntity.patient.id.eq(patientId),
                        prescriptionEntity.doctor.id.eq(doctorId)
                )
                .distinct()
                .fetch();

        return prescriptionEntityList.stream().map(PrescriptionEntity::toDomainModel).toList();
    }

    public Long save(NewPrescription newPrescription) {
        PatientEntity patientEntity = patientJpaRepository.getReferenceById(newPrescription.patientId());
        DoctorEntity doctorEntity = doctorJpaRepository.findByIdWithHospital(newPrescription.doctorId());
        TreatmentEntity treatmentEntity = treatmentJpaRepository.getReferenceById(newPrescription.treatmentId());
        HospitalEntity hospitalEntity = doctorEntity.getHospital();

        PrescriptionEntity prescriptionEntity = new PrescriptionEntity(
                patientEntity,
                doctorEntity,
                hospitalEntity,
                null,
                treatmentEntity,
                newPrescription.date()
        );

        addMedicinesToPrescription(prescriptionEntity, newPrescription.medicines());

        PrescriptionEntity savedPrescriptionEntity = prescriptionJpaRepository.save(prescriptionEntity);
        return savedPrescriptionEntity.getId();
    }

    private void addMedicinesToPrescription(PrescriptionEntity prescriptionEntity, List<PrescriptionMedicine> medicines){
        List<Long> medicineIds = medicines.stream()
                .map(pm -> pm.medicine().id())
                .distinct()
                .toList();

        List<MedicineEntity> medicineEntities = medicineJpaRepository.findAllById(medicineIds);

        Map<Long, MedicineEntity> medicineEntityMap = medicineEntities.stream()
                .collect(Collectors.toMap(MedicineEntity::getId, Function.identity()));

        List<PrescriptionMedicineEntity> prescriptionMedicineEntities = medicines.stream()
                .map(pm -> {
                    MedicineEntity medicineEntity = medicineEntityMap.get(pm.medicine().id());
                    return new PrescriptionMedicineEntity(
                            medicineEntity,
                            prescriptionEntity,
                            pm.dosage(),
                            pm.dosageUnit(),
                            pm.quantity(),
                            pm.frequency(),
                            pm.instruction());
                }).toList();

        prescriptionMedicineEntities.forEach(prescriptionEntity::addPrescriptionMedicine);
    }

    public Optional<Prescription> getPrescriptionById(Long prescriptionId) {
        Optional<PrescriptionEntity> prescriptionEntityOptional = prescriptionJpaRepository.findById(prescriptionId);

        return prescriptionEntityOptional.map(PrescriptionEntity::toDomainModel);
    }
}
