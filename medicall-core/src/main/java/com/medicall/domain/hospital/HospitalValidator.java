package com.medicall.domain.hospital;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class HospitalValidator {

    private final HospitalRepository hospitalRepository;

    public HospitalValidator(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public void validateHospital(Long hospitalId) {
        if(!hospitalRepository.isHospitalExist(hospitalId)){
            throw new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND);
        }
    }
}
