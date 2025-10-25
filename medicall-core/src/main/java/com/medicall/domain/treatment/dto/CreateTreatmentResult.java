package com.medicall.domain.treatment.dto;

import java.time.LocalDateTime;

import com.medicall.domain.treatment.Treatment;

public record CreateTreatmentResult(
        Long treatmentId,
        String patient,
        LocalDateTime createTime
) {
    public static CreateTreatmentResult from(Treatment treatment) {
        return new CreateTreatmentResult(
                treatment.id(),
                treatment.patient().name(),
                treatment.createdAt()
        );
    }
}
