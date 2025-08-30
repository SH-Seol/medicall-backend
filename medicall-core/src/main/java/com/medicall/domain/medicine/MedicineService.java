package com.medicall.domain.medicine;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {

    private final MedicineReader medicineReader;

    public MedicineService(MedicineReader medicineReader) {
        this.medicineReader = medicineReader;
    }

    public List<Medicine> getMedicineList(String keyword) {
        return medicineReader.getMedicineList(keyword);
    }
}
