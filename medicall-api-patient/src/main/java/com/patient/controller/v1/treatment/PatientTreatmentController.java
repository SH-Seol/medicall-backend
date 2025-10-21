package com.patient.controller.v1.treatment;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.treatment.TreatmentService;
import com.medicall.domain.treatment.dto.TreatmentListResult;
import com.medicall.support.CursorPageResult;
import com.patient.controller.v1.treatment.dto.request.PatientTreatmentListRequest;
import com.patient.controller.v1.treatment.dto.response.PatientTreatmentListResponse;

@RestController
@RequestMapping("api/v1/patient/treatment")
public class PatientTreatmentController implements PatientTreatmentApiDocs{

    private final TreatmentService treatmentService;

    public PatientTreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public CursorPageResponse<PatientTreatmentListResponse> getPatientTreatmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                                    @Valid PatientTreatmentListRequest request) {
        CursorPageResult<TreatmentListResult> result = treatmentService.getTreatmentListByPatient(request.toCriteria(currentUser.userId()));

        List<PatientTreatmentListResponse> responses = result.data().stream().map(PatientTreatmentListResponse::from).toList();

        return CursorPageResponse.of(responses, request.cursorId(), result.nextCursorId());
    }
}
