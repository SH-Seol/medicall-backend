package com.patient.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PathVariable;

import com.medicall.common.support.CurrentUser;
import com.patient.controller.v1.doctor.dto.response.PatientDoctorResponse;

public interface PatientDoctorApiDocs {

    @Operation(
            summary = "환자 의사 정보 조회",
            description = "환자가 의사 id를 통해 정보를 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "의사를 찾을 수 없음")
    })
    PatientDoctorResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                    @Parameter(hidden = true) CurrentUser currentUser);
}
