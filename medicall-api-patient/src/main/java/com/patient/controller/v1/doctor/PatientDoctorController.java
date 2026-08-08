package com.patient.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Parameter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.appointment.AppointmentService;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;
import com.patient.controller.v1.doctor.dto.response.AvailableSlotResponse;
import com.patient.controller.v1.doctor.dto.response.PatientDoctorResponse;

@RestController
@RequestMapping("api/v1/patient/doctors")
public class PatientDoctorController implements PatientDoctorApiDocs{

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public PatientDoctorController(DoctorService doctorService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{doctorId}")
    public PatientDoctorResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                           @Parameter(hidden = true) CurrentUser currentUser) {
        DoctorResult doctor = doctorService.findById(doctorId);
        return PatientDoctorResponse.from(doctor);
    }

    /**
     * 예약 가능 시간 조회.
     * 예약은 1시간 단위 정시로만 받으므로 프론트가 슬롯 목록을 그대로 쓰면 된다.
     */
    @GetMapping("/{doctorId}/slots")
    public List<AvailableSlotResponse> getAvailableSlots(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(hidden = true) CurrentUser currentUser) {

        return appointmentService.getAvailableSlots(doctorId, date).stream()
                .map(AvailableSlotResponse::from)
                .toList();
    }
}
