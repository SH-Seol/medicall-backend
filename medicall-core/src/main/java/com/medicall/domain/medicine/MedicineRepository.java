package com.medicall.domain.medicine;

import java.util.List;
import java.util.Set;

public interface MedicineRepository {
    List<Medicine> searchMedicines(String keyword);
    Set<Long> isExistMedicine(List<Long> medicineIds);
}
