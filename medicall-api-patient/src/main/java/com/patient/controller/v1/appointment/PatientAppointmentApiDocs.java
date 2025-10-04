package com.patient.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.patient.controller.v1.appointment.dto.request.CreatePatientAppointmentRequest;
import com.patient.controller.v1.appointment.dto.request.PatientAppointmentListRequest;
import com.patient.controller.v1.appointment.dto.response.CreatePatientAppointmentResponse;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentListResponse;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentDetailResponse;

@Tag(name = "Appointment", description = "환자 예약 관련 API")
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

    @Operation(
            summary = "환자 예약 조회",
            description = "환자 본인의 예약을 선택하여 id를 통해 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "401", description = "인증 문제"),
            @ApiResponse(responseCode = "403", description = "조회 권한 없음"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
    })
    PatientAppointmentDetailResponse getAppointmentById(
            @PathVariable("appointmentId") Long appointmentId,
            @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "환자 예약 요청",
            description = "환자가 예약을 병원에 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    })
    ResponseEntity<CreatePatientAppointmentResponse> createAppointment(
            @RequestBody CreatePatientAppointmentRequest request,
            @Parameter(hidden = true) CurrentUser currentUser
    );
}
