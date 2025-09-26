package com.patient.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentService;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;
import com.patient.controller.v1.appointment.dto.request.PatientAppointmentListRequest;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentListResponse;

@RestController
@RequestMapping("/api/v1/patient/appointments")
@Tag(name = "Patient Appointments API", description = "환자 예약 관리 API")
public class PatientAppointmentController {

    private final AppointmentService appointmentService;

    public PatientAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @Operation(
            summary = "환자 예약 목록 조회",
            description = "환자 본인의 예약 목록을 커서 페이징으로 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    })
    public CursorPageResponse<PatientAppointmentListResponse> getMyAppointmentList(
            @ParameterObject @Valid PatientAppointmentListRequest request,
            CurrentUser currentUser
    ) {
        PatientAppointmentListCriteria criteria = request.toCriteria(currentUser.userId());
        CursorPageResult<Appointment> result = appointmentService.getAppointmentList(criteria);

        List<PatientAppointmentListResponse> appointmentResponseList = result.data().stream().map(
                PatientAppointmentListResponse::from).toList();

        return CursorPageResponse.of(
                appointmentResponseList,
                request.cursorId(),
                result.nextCursorId()
        );
    }

}
