package com.patient.controller.v1.hospital;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.patient.controller.v1.hospital.dto.request.PatientHospitalDetailRequest;
import com.patient.controller.v1.hospital.dto.request.PatientHospitalSearchRequest;
import com.patient.controller.v1.hospital.dto.response.PatientHospitalDetailResponse;
import com.patient.controller.v1.hospital.dto.response.PatientHospitalListResponse;

public interface PatientHospitalApiDocs {
    @Operation(
            summary = "환자 병원 목록 조회",
            description = "환자가 병원 이름이나 진료 과목을 통해 목록을 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    CursorPageResponse<PatientHospitalListResponse> findByHospitalName(@Parameter(hidden = true) CurrentUser currentUser,
                                                                       PatientHospitalSearchRequest request);

    @Operation(
            summary = "환자 병원 상세 정보 조회",
            description = "환자가 병원 ID를 통해 목록을 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "병원을 찾을 수 없음")
    })
    PatientHospitalDetailResponse readHospitalDetail(@PathVariable("hospitalId") Long hospitalId,
                                                     @Valid PatientHospitalDetailRequest request,
                                                     @Parameter(hidden = true) CurrentUser currentUser);
}
