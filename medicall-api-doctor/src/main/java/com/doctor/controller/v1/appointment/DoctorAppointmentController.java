package com.doctor.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.appointment.dto.request.DoctorAppointmentListRequest;
import com.doctor.controller.v1.appointment.dto.response.DoctorAppointmentDetailResponse;
import com.doctor.controller.v1.appointment.dto.response.DoctorAppointmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.appointment.AppointmentService;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.appointment.dto.AppointmentListResult;
import com.medicall.support.CursorPageResult;

@RestController
@RequestMapping("api/v1/doctor/appointments")
public class DoctorAppointmentController implements DoctorAppointmentApiDocs{

    private final AppointmentService appointmentService;

    public DoctorAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    /**
     * 예약 목록 조회
     */
    @GetMapping
    public CursorPageResponse<DoctorAppointmentListResponse> getAppointmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                                @Valid DoctorAppointmentListRequest request){
        CursorPageResult<AppointmentListResult> result = appointmentService.getAppointmentListByDoctor(request.toCriteria(
                currentUser.userId()));
        List<DoctorAppointmentListResponse> responses = result.data().stream().map(DoctorAppointmentListResponse::from).toList();

        return CursorPageResponse.of(responses, request.cursorId(), result.nextCursorId());
    }

    /**
     * 예약 조회
     */
    @GetMapping("/{appointmentId}")
    public DoctorAppointmentDetailResponse getAppointmentDetail(@PathVariable Long appointmentId,
                                                                @Parameter(hidden = true) CurrentUser currentUser) {
        AppointmentDetailResult result = appointmentService.findAppointmentByDoctor(currentUser.userId(), appointmentId);

        return DoctorAppointmentDetailResponse.from(result);
    }
}
