package com.hospital.controller.doctor;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.doctor.dto.response.HospitalDoctorDetailResponse;
import com.hospital.controller.doctor.dto.response.HospitalInvitationResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;
import com.medicall.domain.invitation.DoctorInvitation;
import com.medicall.domain.invitation.DoctorInvitationService;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@RestController
@RequestMapping("api/v1/hospital/doctor")
public class HospitalDoctorController implements HospitalDoctorApiDocs {

    private final DoctorService doctorService;
    private final DoctorInvitationService doctorInvitationService;

    public HospitalDoctorController(DoctorService doctorService,
                                    DoctorInvitationService doctorInvitationService) {
        this.doctorService = doctorService;
        this.doctorInvitationService = doctorInvitationService;
    }

    /**
     * 병원 의사 목록 조회
     */
    @GetMapping
    public List<HospitalDoctorDetailResponse> getDoctors(@Parameter(hidden = true) CurrentUser currentUser) {
        return doctorService.getDoctorsByHospital(currentUser.userId()).stream()
                .map(HospitalDoctorDetailResponse::from)
                .toList();
    }

    /**
     * 의사 조회
     */
    @GetMapping("/{doctorId}")
    public HospitalDoctorDetailResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                                  @Parameter(hidden = true) CurrentUser currentUser) {
        DoctorResult doctor = doctorService.getDoctorsByHospital(currentUser.userId()).stream()
                .filter(result -> result.id().equals(doctorId))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorType.DOCTOR_NOT_FOUND));

        return HospitalDoctorDetailResponse.from(doctor);
    }

    @PostMapping("/invitations")
    public ResponseEntity<HospitalInvitationResponse> invite(@Parameter(hidden = true) CurrentUser currentUser) {
        DoctorInvitation invitation = doctorInvitationService.invite(currentUser.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(HospitalInvitationResponse.from(invitation));
    }

    @GetMapping("/invitations")
    public List<HospitalInvitationResponse> getInvitations(@Parameter(hidden = true) CurrentUser currentUser) {
        return doctorInvitationService.getInvitations(currentUser.userId()).stream()
                .map(HospitalInvitationResponse::from)
                .toList();
    }

    @DeleteMapping("/invitations/{code}")
    public ResponseEntity<Void> cancelInvitation(@PathVariable("code") String code,
                                                 @Parameter(hidden = true) CurrentUser currentUser) {
        doctorInvitationService.cancel(currentUser.userId(), code);

        return ResponseEntity.noContent().build();
    }
}
