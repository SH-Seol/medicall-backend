package com.medicall.domain.treatment;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
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


}
