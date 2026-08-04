package com.hospital.controller.hospital;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.hospital.controller.hospital.dto.request.UpdateHospitalAddressRequest;
import com.hospital.controller.hospital.dto.request.UpdateHospitalDepartmentsRequest;
import com.hospital.controller.hospital.dto.request.UpdateHospitalProfileRequest;
import com.hospital.controller.hospital.dto.request.UpdateOperatingTimesRequest;
import com.hospital.controller.hospital.dto.response.HospitalProfileResponse;
import com.hospital.controller.hospital.dto.response.HospitalSetupStatusResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Hospital", description = "병원 내 정보 API")
public interface HospitalApiDocs {

    @Operation(
            summary = "병원 내 정보 조회",
            description = "로그인한 병원의 내 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    HospitalProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "병원 내 정보 수정",
            description = "로그인한 병원의 내 정보를 수정합니다. 전달하지 않은(null) 항목은 기존 값을 유지합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    HospitalProfileResponse updateMyProfile(UpdateHospitalProfileRequest request,
                                            @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "병원 주간 업무 시간 수정",
            description = "요청한 요일 기준으로 병원의 주간 업무 시간을 교체합니다. 요청에 없는 요일은 삭제됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    HospitalProfileResponse updateOperatingTimes(UpdateOperatingTimesRequest request,
                                                 @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "병원 주소 등록·수정",
            description = "온보딩 단계에서 병원 주소를 등록합니다. 이미 등록된 주소가 있으면 교체됩니다. "
                    + "환자의 주변 병원 검색에 사용되므로 위도·경도가 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    HospitalProfileResponse updateAddress(UpdateHospitalAddressRequest request,
                                          @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "병원 진료과 등록",
            description = "병원이 운영하는 진료과를 등록합니다. 전달한 목록으로 전체 교체됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "병원 또는 진료과를 찾을 수 없음")
    })
    HospitalProfileResponse updateDepartments(UpdateHospitalDepartmentsRequest request,
                                              @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "온보딩 진행 상태 조회",
            description = "주소·진료과·업무 시간 등록 여부와 온보딩 완료 여부를 반환합니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    HospitalSetupStatusResponse getSetupStatus(@Parameter(hidden = true) CurrentUser currentUser);
}
