package com.doctor.controller.v1.treatment;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.treatment.dto.request.CreateDoctorTreatmentRequest;
import com.doctor.controller.v1.treatment.dto.request.DoctorTreatmentListRequest;
import com.doctor.controller.v1.treatment.dto.response.CreateDoctorTreatmentResponse;
import com.doctor.controller.v1.treatment.dto.response.DoctorTreatmentDetailResponse;
import com.doctor.controller.v1.treatment.dto.response.DoctorTreatmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.treatment.TreatmentService;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;
import com.medicall.domain.treatment.dto.TreatmentListResult;
import com.medicall.support.CursorPageResult;

@RestController
@RequestMapping("api/v1/doctor/treatments")
public class DoctorTreatmentController implements DoctorTreatmentApiDocs{

    private final TreatmentService treatmentService;

    public DoctorTreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }
    /**
     * 진료 기록 생성
     */
    @PostMapping
    public ResponseEntity<CreateDoctorTreatmentResponse> createTreatment(@RequestBody CreateDoctorTreatmentRequest createDoctorTreatmentRequest,
                                                                         @Parameter(hidden = true) CurrentUser currentUser){
        return null;
    }

    /**
     * 환자 진료 목록 조회
     */
    @GetMapping("{/patients/{patientId}")
    public CursorPageResponse<DoctorTreatmentListResponse> getPatientTreatmentList (@PathVariable Long patientId,
                                                                                    @Valid DoctorTreatmentListRequest request,
                                                                                    @Parameter(hidden = true) CurrentUser currentUser) {
        CursorPageResult<TreatmentListResult> result = treatmentService.getTreatmentListByDoctorAndPatient(request.toCriteria(
                currentUser.userId(), patientId));
        List<DoctorTreatmentListResponse> responses = result.data().stream().map(DoctorTreatmentListResponse::from).toList();
        return CursorPageResponse.of(responses, request.cursorId(), result.nextCursorId());
    }

    /**
     * 환자 진료 기록 단건 조회
     */
    @GetMapping("{treatmentId}")
    public DoctorTreatmentDetailResponse getTreatmentDetail (@PathVariable Long treatmentId,
                                                             @Parameter(hidden = true) CurrentUser currentUser) {
        TreatmentDetailResult result = treatmentService.getTreatmentByDoctor(currentUser.userId(), treatmentId);

        return DoctorTreatmentDetailResponse.from(result);
    }
}
