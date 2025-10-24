package com.doctor.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.doctor.controller.v1.prescription.dto.request.CreateDoctorPrescriptionRequest;
import com.doctor.controller.v1.prescription.dto.response.CreateDoctorPrescriptionResponse;
import com.doctor.controller.v1.prescription.dto.response.DoctorPrescriptionDetailResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Prescription", description = "의사 처방 관련 API")
public interface DoctorPrescriptionApiDocs {

    @Operation(
            summary = "처방 생성 요청",
            description = "의사가 환자에 대한 처방전 생성을 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "생성 권한 없음")
    })
    ResponseEntity<CreateDoctorPrescriptionResponse> createPrescription(@RequestBody CreateDoctorPrescriptionRequest request,
                                                                        @Parameter(hidden = true) CurrentUser currentUser);
    @Operation(
            summary = "처방 상세 조회",
            description = "의사 진료 내역 중 하나를 선택하여 id를 통해 처방전을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "처방전을 찾을 수 없음")
    })
    DoctorPrescriptionDetailResponse getDoctorPrescriptionDetailById(@PathVariable Long prescriptionId,
                                                                     @Parameter(hidden = true) CurrentUser currentUser);
}
