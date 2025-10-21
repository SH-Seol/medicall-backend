package com.patient.controller.v1.treatment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.patient.controller.v1.treatment.dto.request.PatientTreatmentListRequest;
import com.patient.controller.v1.treatment.dto.response.PatientTreatmentListResponse;


@Tag(name = "Prescription", description = "환자 처방 관련 API")
public interface PatientTreatmentApiDocs {

    @Operation(
            summary = "환자 진료 목록 조회",
            description = "환자가 진료 목록을 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "진료를 찾을 수 없음")
    })
    CursorPageResponse<PatientTreatmentListResponse> getPatientTreatmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                             @Valid PatientTreatmentListRequest request);
}
