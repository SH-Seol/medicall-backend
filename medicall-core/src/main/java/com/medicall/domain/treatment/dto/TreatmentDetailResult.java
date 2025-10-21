package com.medicall.domain.treatment.dto;

import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.treatment.Treatment;

public record TreatmentDetailResult(
        Patient patient,
        Doctor doctor,
        Hospital hospital,
        String symptoms,
        String treatment,
        String detailedTreatment,
        Long prescriptionId
) {
    public static TreatmentDetailResult from(Treatment treatment){
        return new TreatmentDetailResult(
                treatment.patient(),
                treatment.doctor(),
                treatment.hospital(),
                treatment.symptoms(),
                treatment.treatment(),
                treatment.detailedTreatment(),
                treatment.prescription().id()
        );
    }
}
