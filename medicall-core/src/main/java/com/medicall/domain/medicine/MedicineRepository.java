package com.medicall.domain.medicine;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository {
    List<Medicine> searchMedicines(String keyword);
    Set<Long> isExistMedicine(List<Long> medicineIds);
}
