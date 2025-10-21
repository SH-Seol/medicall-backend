package com.medicall.domain.treatment;

import com.medicall.domain.treatment.dto.CreatePrescriptionRequest;
import com.medicall.domain.treatment.dto.HospitalTreatmentListCriteria;
import com.medicall.domain.treatment.dto.PatientTreatmentListCriteria;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;
import com.medicall.domain.treatment.dto.TreatmentListResult;
import com.medicall.support.CursorPageResult;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TreatmentService {

    private final TreatmentReader treatmentReader;
    private final TreatmentWriter treatmentWriter;
    private final TreatmentValidator treatmentValidator;

    public TreatmentService(TreatmentReader treatmentReader, TreatmentWriter treatmentWriter,
                            TreatmentValidator treatmentValidator) {
        this.treatmentReader = treatmentReader;
        this.treatmentWriter = treatmentWriter;
        this.treatmentValidator = treatmentValidator;
    }

    //진료 기록 작성
    public Long addTreatment(Long doctorId, CreatePrescriptionRequest request) {
        NewTreatment treatment = new NewTreatment(
                request.patientId(),
                doctorId,
                request.symptoms(),
                request.treatment(),
                request.detailedTreatment());
        return treatmentWriter.addTreatment(treatment);
    }

    /**
     * 진료 기록 상세 조회(병원, 의사만 가능)
     */
    public TreatmentDetailResult getTreatmentByHospital(Long hospitalId, Long treatmentId) {
        Treatment treatment = treatmentReader.findById(treatmentId);
        treatmentValidator.validateHospitalTreatment(treatment, hospitalId);

        return TreatmentDetailResult.from(treatment);
    }

    public TreatmentDetailResult getTreatmentByDoctor(Long doctorId, Long treatmentId) {
        Treatment treatment = treatmentReader.findById(treatmentId);
        treatmentValidator.validateDoctorTreatment(treatment, doctorId);

        return TreatmentDetailResult.from(treatment);
    }

    public CursorPageResult<TreatmentListResult> getTreatmentListByPatient(PatientTreatmentListCriteria criteria){
        return treatmentReader.getTreatmentListByPatient(criteria);
    }

    public CursorPageResult<TreatmentListResult> getTreatmentListByHospitalAndPatient(HospitalTreatmentListCriteria criteria){
        return null;
    }
}
