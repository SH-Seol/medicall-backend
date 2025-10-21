package com.medicall.domain.prescription;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class PrescriptionValidator {

    public void validatePatientPrescription(Prescription prescription, Long patientId) {
        if(!prescription.patient().id().equals(patientId)){
            throw new CoreException(CoreErrorType.PRESCRIPTION_NOT_ACCESSIBLE);
        }
    }

    public void validateHospitalPrescription(Prescription prescription, Long hospitalId) {
        if(!prescription.hospital().id().equals(hospitalId)){
            throw new CoreException(CoreErrorType.PRESCRIPTION_NOT_ACCESSIBLE);
        }
    }

    public void validateDoctorPrescription(Prescription prescription, Long doctorId) {
        if(!prescription.doctor().id().equals(doctorId)){
            throw new CoreException(CoreErrorType.PRESCRIPTION_NOT_ACCESSIBLE);
        }
    }
}
