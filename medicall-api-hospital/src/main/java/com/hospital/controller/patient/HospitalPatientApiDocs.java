package com.hospital.controller.patient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.controller.patient.dto.response.HospitalPatientDetailResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Patient", description = "병원 환자 조회 API")
public interface HospitalPatientApiDocs {

    @Operation(
            summary = "병원 환자 정보 조회",
            description = "병원이 환자 id를 통해 정보를 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "환자를 찾을 수 없음")
    })
    HospitalPatientDetailResponse getPatientDetail(@PathVariable Long patientId,
                                                   @Parameter(hidden = true) CurrentUser currentUser);
}
