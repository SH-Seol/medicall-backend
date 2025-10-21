package com.hospital.controller.treatment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.controller.treatment.dto.request.HospitalTreatmentListRequest;
import com.hospital.controller.treatment.dto.response.HospitalTreatmentDetailResponse;
import com.hospital.controller.treatment.dto.response.HospitalTreatmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;

@Tag(name = "Treatment", description = "")
public interface HospitalTreatmentApiDocs {

    @Operation(
            summary = "진료 상세 조회",
            description = "병원 진료 내역 중 하나를 선택하여 id를 통해 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "진료를 찾을 수 없음")
    })
    HospitalTreatmentDetailResponse getDetailHospitalTreatment(@PathVariable Long treatmentId,
                                                               @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "진료 목록 조회",
            description = "병원 진료 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "진료를 찾을 수 없음")
    })
    CursorPageResponse<HospitalTreatmentListResponse> getHospitalTreatmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                               @Valid HospitalTreatmentListRequest request);
}
