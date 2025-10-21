package com.medicall.domain.treatment;

import com.medicall.domain.treatment.dto.PatientTreatmentListCriteria;
import com.medicall.domain.treatment.dto.TreatmentListResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.support.CorePageUtils;
import com.medicall.support.CursorPageResult;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TreatmentReader {

    private final TreatmentRepository treatmentRepository;

    public TreatmentReader(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    //환자 id 로 진료 기록 불러오기
    public List<Treatment> getTreatmentsByPatientId(Long patientId, Long doctorId) {
        return treatmentRepository.getTreatmentsByPatientId(patientId, doctorId);
    }

    public Treatment findById(Long treatmentId){
        return treatmentRepository.findById(treatmentId).orElseThrow(() -> new CoreException(CoreErrorType.TREATMENT_NOT_FOUND));
    }

    public CursorPageResult<TreatmentListResult> getTreatmentListByPatient(PatientTreatmentListCriteria criteria) {
        List<Treatment> treatments = treatmentRepository.findAllByPatientId(criteria.patientId(), criteria.cursorId(), criteria.size());
        List<TreatmentListResult> result = treatments
                .stream()
                .map(treatment ->
                        new TreatmentListResult(
                                treatment.id(),
                                treatment.patient(),
                                treatment.hospital(),
                                treatment.doctor(),
                                treatment.createdAt(),
                                treatment.prescription().id()
                        )
                ).toList();
        return CorePageUtils.buildCursorResult(result, criteria.size(), TreatmentListResult::treatmentId);
    }


}
