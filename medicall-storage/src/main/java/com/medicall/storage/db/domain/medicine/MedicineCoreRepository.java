package com.medicall.storage.db.domain.medicine;

import com.medicall.domain.medicine.Medicine;
import com.medicall.domain.medicine.MedicineRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class MedicineCoreRepository implements MedicineRepository {

    private final MedicineJpaRepository medicineJpaRepository;

    public MedicineCoreRepository(MedicineJpaRepository medicineJpaRepository) {
        this.medicineJpaRepository = medicineJpaRepository;
    }

    public List<Medicine> searchMedicines(String keyword) {
        List<MedicineEntity> medicineEntities = medicineJpaRepository.findTop5ByNameContaining(keyword);

        return medicineEntities.stream().map(MedicineEntity::toDomainModel).toList();
    }

    public Set<Long> isExistMedicine(List<Long> medicineIds) {
        List<MedicineEntity> foundMedicines = medicineJpaRepository.findAllById(medicineIds);

        return foundMedicines.stream().
                map(MedicineEntity::getId).collect(Collectors.toSet());
    }
}
