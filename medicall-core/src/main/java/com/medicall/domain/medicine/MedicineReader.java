package com.medicall.domain.medicine;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MedicineReader {

    private final MedicineRepository medicineRepository;

    public MedicineReader(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> getMedicineList(String keyword) {
        return medicineRepository.searchMedicines(keyword);
    }
}
