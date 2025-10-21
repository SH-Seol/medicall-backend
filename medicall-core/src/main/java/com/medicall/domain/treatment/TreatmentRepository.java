package com.medicall.domain.treatment;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository {
    List<Treatment> getTreatmentsByPatientId(Long patientId, Long doctorId);
    Long save(NewTreatment treatment);
    Optional<Treatment> findById(Long treatmentId);
    List<Treatment> findAllByPatientId(Long patientId, Long cursorId, int size);
}
