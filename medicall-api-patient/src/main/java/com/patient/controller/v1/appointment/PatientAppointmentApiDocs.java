package com.patient.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.patient.controller.v1.appointment.dto.request.PatientAppointmentListRequest;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentListResponse;

@Tag(name = "Patient Prescription API", description = "Patient Prescription Endpoints")
public interface PatientAppointmentApiDocs {

    @Operation(
            summary = "환자 예약 목록 조회",
            description = "환자 본인의 예약 목록을 커서 페이징으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    })
    CursorPageResponse<PatientAppointmentListResponse> getMyAppointmentList(
            @ParameterObject @Valid PatientAppointmentListRequest request,
            @Parameter(hidden = true) CurrentUser currentUser
    );
}
