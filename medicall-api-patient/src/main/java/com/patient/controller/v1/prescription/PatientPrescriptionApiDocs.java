package com.patient.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;

import com.medicall.common.support.CurrentUser;
import com.patient.controller.v1.prescription.dto.request.PatientPrescriptionListRequest;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionDetailResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionListResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionQrResponse;

@Tag(name = "Prescription", description = "환자 처방 관련 API")
public interface PatientPrescriptionApiDocs {

    @Operation(
            summary = "처방전 상세 조회",
            description = "환자 본인의 처방전을 선택하여 id를 통해 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "처방전을 찾을 수 없음")
    })
    PatientPrescriptionDetailResponse getPrescriptionDetail(@PathVariable("prescriptionId") Long prescriptionId,
                                                            @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "환자 처방 목록 조회",
            description = "환자 본인의 처방 목록을 처방id를 통해 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음")
    })
    PatientPrescriptionListResponse getPrescriptionList(@Parameter(hidden = true) CurrentUser currentUser,
                                                        @Valid PatientPrescriptionListRequest request);

    @Operation(
            summary = "환자 처방 QR 코드 가져오기",
            description = "환자 본인의 처방전의 QR코드를 처방 id를 통해 가져옵니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "처방전을 찾을 수 없음")
    })
    PatientPrescriptionQrResponse getPrescriptionQrCode(@PathVariable("prescriptionId") Long prescriptionId,
                                                        @Parameter(hidden = true) CurrentUser currentUser);
}
