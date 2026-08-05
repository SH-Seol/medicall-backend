package com.doctor.controller.v1.hospital;

import io.swagger.v3.oas.annotations.Parameter;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.hospital.dto.response.DoctorInvitationResponse;
import com.medicall.common.security.RateLimiter;
import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.invitation.DoctorInvitation;
import com.medicall.domain.invitation.DoctorInvitationService;

@RestController
@RequestMapping("api/v1/doctor/hospitals")
public class DoctorHospitalController implements DoctorHospitalApiDocs {

    /** 코드 추측 시도를 막기 위한 제한 (의사 1명당 10분에 10회) */
    private static final int INVITATION_ATTEMPT_LIMIT = 10;
    private static final Duration INVITATION_ATTEMPT_WINDOW = Duration.ofMinutes(10);

    private final DoctorInvitationService doctorInvitationService;
    private final RateLimiter rateLimiter;

    public DoctorHospitalController(DoctorInvitationService doctorInvitationService, RateLimiter rateLimiter) {
        this.doctorInvitationService = doctorInvitationService;
        this.rateLimiter = rateLimiter;
    }

    private void checkAttemptLimit(CurrentUser currentUser) {
        if(rateLimiter.isExceeded("invitation:doctor:" + currentUser.userId(),
                INVITATION_ATTEMPT_LIMIT, INVITATION_ATTEMPT_WINDOW)){
            throw new AuthException(AuthErrorType.TOO_MANY_REQUESTS);
        }
    }

    /**
     * 초대 코드로 링크 접속한 경우
     */
    @GetMapping("/invitations/{code}")
    public DoctorInvitationResponse getInvitation(@PathVariable("code") String code,
                                                  @Parameter(hidden = true) CurrentUser currentUser) {
        checkAttemptLimit(currentUser);

        DoctorInvitation invitation = doctorInvitationService.preview(code);

        return DoctorInvitationResponse.from(invitation);
    }

    @PostMapping("/invitations/{code}/accept")
    public DoctorInvitationResponse acceptInvitation(@PathVariable("code") String code,
                                                     @Parameter(hidden = true) CurrentUser currentUser) {
        checkAttemptLimit(currentUser);

        DoctorInvitation invitation = doctorInvitationService.accept(code, currentUser.userId());

        return DoctorInvitationResponse.from(invitation);
    }
}
