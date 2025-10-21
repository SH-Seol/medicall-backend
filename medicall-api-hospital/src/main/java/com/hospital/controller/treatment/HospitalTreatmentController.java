package com.hospital.controller.treatment;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.treatment.dto.request.HospitalTreatmentListRequest;
import com.hospital.controller.treatment.dto.response.HospitalTreatmentDetailResponse;
import com.hospital.controller.treatment.dto.response.HospitalTreatmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.treatment.TreatmentService;
import com.medicall.domain.treatment.dto.TreatmentDetailResult;
import com.medicall.domain.treatment.dto.TreatmentListResult;
import com.medicall.support.CursorPageResult;

@RestController
@RequestMapping("api/v1/hospital/treatment")
public class HospitalTreatmentController {

    private final TreatmentService treatmentService;

    public HospitalTreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    /**
     * 진료 조회
     */
    @GetMapping("{treatmentId}")
    public HospitalTreatmentDetailResponse getDetailHospitalTreatment(@PathVariable Long treatmentId,
                                                                      @Parameter(hidden = true) CurrentUser currentUser){
        TreatmentDetailResult result = treatmentService.getTreatmentByHospital(currentUser.userId(), treatmentId);

        return HospitalTreatmentDetailResponse.from(result);
    }

    /**
     * 진료 목록 조회
     */
    @GetMapping
    public CursorPageResponse<HospitalTreatmentListResponse> getHospitalTreatmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                                      @Valid HospitalTreatmentListRequest request){
        CursorPageResult<TreatmentListResult> result = treatmentService.getTreatmentListByHospital(request.toCriteria(
                currentUser.userId()));
        List<HospitalTreatmentListResponse> responses = result.data().stream().map(HospitalTreatmentListResponse::from).toList();
        return CursorPageResponse.of(responses, request.cursorId(), result.nextCursorId());
    }
}
