package com.medicall.domain.doctor;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class DoctorValidator {

    private final DoctorRepository doctorRepository;

    public DoctorValidator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void validateDoctorBelongsToHospital(Long doctorId){
        if(doctorRepository.isDoctorBelongsToHospital(doctorId)){
            throw new CoreException(CoreErrorType.DOCTOR_BELONGS_TO_HOSPITAL);
        }
    }

    public void validateDoctor(Long doctorId){
        boolean result = doctorRepository.isDoctorExist(doctorId);

        if(!result){
            throw new CoreException(CoreErrorType.DOCTOR_NOT_FOUND);
        }
    }
}
