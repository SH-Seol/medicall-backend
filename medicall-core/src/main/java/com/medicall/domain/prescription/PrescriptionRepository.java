package com.medicall.domain.prescription;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.medicall.domain.prescription.dto.PatientPrescriptionListCriteria;
import com.medicall.support.CursorPageResult;

@Repository
public interface PrescriptionRepository {
    List<Prescription> getPrescriptionByPatientIdAndDoctorId(Long patientId, Long doctorId);
    Prescription save(NewPrescription newPrescription);
    Optional<Prescription> getPrescriptionById(Long prescriptionId);
    CursorPageResult<Prescription> findByPatientId(PatientPrescriptionListCriteria criteria);
}
