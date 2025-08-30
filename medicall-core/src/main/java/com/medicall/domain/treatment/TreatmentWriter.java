package com.medicall.domain.treatment;

import org.springframework.stereotype.Component;

@Component
public class TreatmentWriter {

    private final TreatmentRepository treatmentRepository;

    public TreatmentWriter(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    //환자 treatment 작성
    public Long addTreatment(NewTreatment treatment) {
        return treatmentRepository.save(treatment);
    }
}
