package com.medicall.domain.prescription;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository {
    List<Prescription> getPrescriptionByPatientIdAndDoctorId(Long patientId, Long doctorId);
    Prescription save(NewPrescription newPrescription);
    Optional<Prescription> getPrescriptionById(Long prescriptionId);
}
