package com.patient.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.appointment.AppointmentService;
import com.medicall.domain.appointment.dto.CreateAppointmentResult;
import com.medicall.domain.appointment.dto.PatientAppointmentListCriteria;
import com.medicall.support.CursorPageResult;
import com.patient.controller.v1.appointment.dto.request.CreatePatientAppointmentRequest;
import com.patient.controller.v1.appointment.dto.request.PatientAppointmentListRequest;
import com.patient.controller.v1.appointment.dto.response.CreatePatientAppointmentResponse;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentListResponse;
import com.patient.controller.v1.appointment.dto.response.PatientAppointmentDetailResponse;

@RestController
@RequestMapping("/api/v1/patient/appointments")
@Tag(name = "Patient Appointments API", description = "환자 예약 관리 API")
public class PatientAppointmentController implements PatientAppointmentApiDocs {

    private final AppointmentService appointmentService;

    public PatientAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public CursorPageResponse<PatientAppointmentListResponse> getMyAppointmentList(
            @ParameterObject @Valid PatientAppointmentListRequest request,
            @Parameter(hidden = true) CurrentUser currentUser
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

    @GetMapping("/{appointmentId}")
    public PatientAppointmentDetailResponse getAppointmentById(
            @PathVariable("appointmentId") Long appointmentId,
            @Parameter(hidden = true) CurrentUser currentUser)
    {
        Appointment result = appointmentService.findByAppointmentId(currentUser.userId(), appointmentId);
        return PatientAppointmentDetailResponse.from(result);
    }

    @PostMapping
    public ResponseEntity<CreatePatientAppointmentResponse> createAppointment(
            @RequestBody CreatePatientAppointmentRequest request,
            @Parameter(hidden = true) CurrentUser currentUser
    ){
        CreateAppointmentResult result = appointmentService.createAppointment(currentUser.userId(),request.toNewAppointment());
        return ResponseEntity.ok(CreatePatientAppointmentResponse.from(result));
    }

}
