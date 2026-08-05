package com.doctor.controller.v1.appointment;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    /**
     * 방문 출발 ("다음 환자")
     * 이 시점부터 환자에게 출발 알림과 실시간 위치를 제공할 수 있다.
     */
    @PatchMapping("/{appointmentId}/depart")
    public DoctorAppointmentDetailResponse departToPatient(@PathVariable Long appointmentId,
                                                           @Parameter(hidden = true) CurrentUser currentUser){
        AppointmentDetailResult result = appointmentService.departByDoctor(appointmentId, currentUser.userId());

        return DoctorAppointmentDetailResponse.from(result);
    }

    /**
     * 환자 위치 도착
     */
    @PatchMapping("/{appointmentId}/arrive")
    public DoctorAppointmentDetailResponse arriveAtPatient(@PathVariable Long appointmentId,
                                                           @Parameter(hidden = true) CurrentUser currentUser){
        AppointmentDetailResult result = appointmentService.arriveByDoctor(appointmentId, currentUser.userId());

        return DoctorAppointmentDetailResponse.from(result);
    }

    /**
     * 진료 시작
     */
    @PatchMapping("/{appointmentId}/start-treatment")
    public DoctorAppointmentDetailResponse startTreatment(@PathVariable Long appointmentId,
                                                          @Parameter(hidden = true) CurrentUser currentUser){
        AppointmentDetailResult result = appointmentService.startTreatmentByDoctor(appointmentId, currentUser.userId());

        return DoctorAppointmentDetailResponse.from(result);
    }

    /**
     * 진료 완료
     */
    @PatchMapping("/{appointmentId}/complete")
    public DoctorAppointmentDetailResponse completeTreatment(@PathVariable Long appointmentId,
                                                             @Parameter(hidden = true) CurrentUser currentUser){
        AppointmentDetailResult result = appointmentService.completeByDoctor(appointmentId, currentUser.userId());

        return DoctorAppointmentDetailResponse.from(result);
    }
}
