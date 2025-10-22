package com.doctor.controller.v1.patient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;

import com.doctor.controller.v1.patient.dto.response.DoctorPatientDetailResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Patient", description = "의사 환자 관련 API")
public interface DoctorPatientApiDocs {
    @Operation(
            summary = "의사 환자 상세 조회",
            description = "환자 id를 통해 환자 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "환자를 찾을 수 없음")
    })
    DoctorPatientDetailResponse getPatientDetailById(@PathVariable Long patientId,
                                                     @Parameter(hidden = true) CurrentUser currentUser);
}
