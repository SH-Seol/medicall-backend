package com.doctor.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.doctor.controller.v1.doctor.dto.request.UpdateDoctorProfileRequest;
import com.doctor.controller.v1.doctor.dto.response.DoctorProfileResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Doctor", description = "의사 내 정보 API")
public interface DoctorApiDocs {

    @Operation(
            summary = "의사 내 정보 조회",
            description = "로그인한 의사의 내 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "의사를 찾을 수 없음")
    })
    DoctorProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "의사 내 정보 수정",
            description = "로그인한 의사의 내 정보를 수정합니다. 전달하지 않은(null) 항목은 기존 값을 유지합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "의사 또는 진료과를 찾을 수 없음")
    })
    DoctorProfileResponse updateMyProfile(UpdateDoctorProfileRequest request,
                                          @Parameter(hidden = true) CurrentUser currentUser);
}
