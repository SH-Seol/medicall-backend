package com.doctor.controller.v1.hospital;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.hospital.dto.response.DoctorInvitationResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.invitation.DoctorInvitation;
import com.medicall.domain.invitation.DoctorInvitationService;

@RestController
@RequestMapping("api/v1/doctor/hospitals")
public class DoctorHospitalController implements DoctorHospitalApiDocs {

    private final DoctorInvitationService doctorInvitationService;

    public DoctorHospitalController(DoctorInvitationService doctorInvitationService) {
        this.doctorInvitationService = doctorInvitationService;
    }

    /**
     * 초대 코드로 링크 접속한 경우
     */
    @GetMapping("/invitations/{code}")
    public DoctorInvitationResponse getInvitation(@PathVariable("code") String code) {
        DoctorInvitation invitation = doctorInvitationService.preview(code);

        return DoctorInvitationResponse.from(invitation);
    }

    @PostMapping("/invitations/{code}/accept")
    public DoctorInvitationResponse acceptInvitation(@PathVariable("code") String code,
                                                     @Parameter(hidden = true) CurrentUser currentUser) {
        DoctorInvitation invitation = doctorInvitationService.accept(code, currentUser.userId());

        return DoctorInvitationResponse.from(invitation);
    }
}
