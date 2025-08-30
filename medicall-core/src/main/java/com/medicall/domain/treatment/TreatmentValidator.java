package com.medicall.domain.treatment;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import org.springframework.stereotype.Component;

@Component
public class TreatmentValidator {

    private final TreatmentRepository treatmentRepository;

    public TreatmentValidator(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    public void validatePrescriptionCreation(Treatment treatment, Long doctorId) {
        if(treatment.prescription() != null){
            throw new CoreException(CoreErrorType.TREATMENT_ALREADY_HAS_PRESCRIPTION);
        }

        if(!treatment.doctor().id().equals(doctorId)){
            throw new CoreException(CoreErrorType.DOCTOR_IN_TREATMENT_NOT_MATCH);
        }
    }
}
