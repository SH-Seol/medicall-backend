package com.medicall.domain.medicine;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class MedicineValidator {

    private final MedicineRepository medicineRepository;

    public MedicineValidator(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public void validateMedicines(List<Long> medicines) {
        Set<Long> foundIds = medicineRepository.isExistMedicine(medicines);

        if(foundIds.size() != medicines.size()){
            List<Long> missingIds = medicines.stream().filter(id -> !foundIds.contains(id)).toList();
            throw new CoreException(CoreErrorType.MEDICINE_NOT_FOUND, "존재하지 않는 약품 id: " + missingIds);
        }
    }
}
