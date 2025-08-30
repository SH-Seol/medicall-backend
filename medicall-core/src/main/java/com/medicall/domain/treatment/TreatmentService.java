package com.medicall.domain.treatment;

import com.medicall.domain.treatment.dto.CreatePrescriptionRequest;
import com.medicall.domain.treatment.dto.ReadTreatmentResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TreatmentService {

    private final TreatmentReader treatmentReader;
    private final TreatmentWriter treatmentWriter;

    public TreatmentService(TreatmentReader treatmentReader, TreatmentWriter treatmentWriter) {
        this.treatmentReader = treatmentReader;
        this.treatmentWriter = treatmentWriter;
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

    //진단 기록 불러오기
    public List<ReadTreatmentResponse> getTreatments(Long patientId, Long doctorId) {
        List<Treatment> treatments = treatmentReader.getTreatmentsByPatientId(patientId, doctorId);

        return treatments.stream().map(treatment ->
                new ReadTreatmentResponse(
                        treatment.patient(),
                        treatment.doctor(),
                        treatment.symptoms(),
                        treatment.treatment(),
                        treatment.detailedTreatment(),
                        treatment.prescription().id()
                        )).toList();
    }
}
