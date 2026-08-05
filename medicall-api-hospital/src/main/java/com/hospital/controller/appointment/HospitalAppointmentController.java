package com.hospital.controller.appointment;

import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.appointment.dto.request.HospitalAppointmentListRequest;
import com.hospital.controller.appointment.dto.request.AssignDoctorRequest;
import com.hospital.controller.appointment.dto.response.HospitalAppointmentAcceptResponse;
import com.hospital.controller.appointment.dto.response.HospitalAppointmentDetailResponse;
import com.hospital.controller.appointment.dto.response.HospitalAppointmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.appointment.AppointmentService;
import com.medicall.domain.appointment.dto.AppointmentDetailResult;
import com.medicall.domain.appointment.dto.AppointmentListResult;
import com.medicall.support.CursorPageResult;

@RestController
@RequestMapping("api/v1/hospital/appointment")
public class HospitalAppointmentController {

    private final AppointmentService appointmentService;

    public HospitalAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * 예약 상세 조회
     */
    @GetMapping("/{appointmentId}")
    public HospitalAppointmentDetailResponse getAppointmentDetail(@PathVariable Long appointmentId,
                                                                  @Parameter(hidden = true) CurrentUser currentUser) {
        AppointmentDetailResult result = appointmentService.findAppointmentByHospital(currentUser.userId(), appointmentId);

        return HospitalAppointmentDetailResponse.from(result);
    }

    /**
     * 예약 목록 조회
     */
    @GetMapping
    public CursorPageResponse<HospitalAppointmentListResponse> getAppointmentList(@Parameter(hidden = true) CurrentUser currentUser,
                                                                                  @Valid HospitalAppointmentListRequest request) {
        CursorPageResult<AppointmentListResult> result = appointmentService.getAppointmentListByHospital(request.toCriteria(
                currentUser.userId()));

        List<HospitalAppointmentListResponse> responses = result.data().stream().map(HospitalAppointmentListResponse::from).toList();

        return CursorPageResponse.of(responses, request.cursorId(), result.nextCursorId());
    }

    /**
     * 예약 수락
     */
    @PatchMapping("/{appointmentId}")
    public HospitalAppointmentAcceptResponse acceptAppointmentById(@PathVariable Long appointmentId, @Parameter(hidden = true) CurrentUser currentUser) {
        appointmentService.acceptAppointmentByHospital(appointmentId, currentUser.userId());
        return new HospitalAppointmentAcceptResponse(appointmentId, LocalDateTime.now());
    }

    /**
     * 예약 거절
     */
    @PatchMapping("/{appointmentId}/reject")
    public ResponseEntity<Void> rejectAppointmentById(@PathVariable Long appointmentId,
                                                      @Parameter(hidden = true) CurrentUser currentUser) {
        appointmentService.rejectAppointmentByHospital(appointmentId, currentUser.userId());

        return ResponseEntity.noContent().build();
    }

    /**
     * 의사 미지정 예약에 의사 배정
     */
    @PatchMapping("/{appointmentId}/doctor")
    public HospitalAppointmentDetailResponse assignDoctor(@PathVariable Long appointmentId,
                                                          @Valid @RequestBody AssignDoctorRequest request,
                                                          @Parameter(hidden = true) CurrentUser currentUser) {
        AppointmentDetailResult result = appointmentService.assignDoctorByHospital(
                appointmentId, currentUser.userId(), request.doctorId());

        return HospitalAppointmentDetailResponse.from(result);
    }

}
