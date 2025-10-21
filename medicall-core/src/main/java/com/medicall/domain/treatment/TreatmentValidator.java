package com.medicall.domain.treatment;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TreatmentValidator {

    public void validatePrescriptionCreation(Treatment treatment, Long doctorId) {
        if(treatment.prescription() != null){
            throw new CoreException(CoreErrorType.TREATMENT_ALREADY_HAS_PRESCRIPTION);
        }

        if(!treatment.doctor().id().equals(doctorId)){
            throw new CoreException(CoreErrorType.DOCTOR_IN_TREATMENT_NOT_MATCH);
        }
    }

    public void validateHospitalTreatment(Treatment treatment, Long hospitalId) {
        if(!treatment.hospital().id().equals(hospitalId)){
            throw new CoreException(CoreErrorType.TREATMENT_NOT_ACCESSIBLE);
        }
    }

    public void validateDoctorTreatment(Treatment treatment, Long doctorId) {
        if(!treatment.doctor().id().equals(doctorId)){
            throw new CoreException(CoreErrorType.TREATMENT_NOT_ACCESSIBLE);
        }
    }
}
