package com.patient.controller.v1.patient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.medicall.common.support.CurrentUser;
import com.patient.controller.v1.patient.dto.request.UpdatePatientProfileRequest;
import com.patient.controller.v1.patient.dto.response.PatientProfileResponse;

@Tag(name = "Patient", description = "환자 내 정보 API")
public interface PatientApiDocs {

    @Operation(
            summary = "환자 내 정보 조회",
            description = "로그인한 환자의 내 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "환자를 찾을 수 없음")
    })
    PatientProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "환자 내 정보 수정",
            description = "로그인한 환자의 내 정보를 수정합니다. 전달하지 않은(null) 항목은 기존 값을 유지하며, "
                    + "만성 질환 목록은 전달 시 전체 교체됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "환자를 찾을 수 없음")
    })
    PatientProfileResponse updateMyProfile(UpdatePatientProfileRequest request,
                                           @Parameter(hidden = true) CurrentUser currentUser);
}
