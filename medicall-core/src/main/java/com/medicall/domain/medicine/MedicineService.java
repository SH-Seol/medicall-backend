package com.medicall.domain.medicine;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicineService {

    private final MedicineReader medicineReader;

    public MedicineService(MedicineReader medicineReader) {
        this.medicineReader = medicineReader;
    }

    @Transactional(readOnly = true)
    public List<Medicine> getMedicineList(String keyword) {
        return medicineReader.getMedicineList(keyword);
    }
}
