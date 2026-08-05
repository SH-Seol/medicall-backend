package com.patient.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.security.DevLoginResponse;
import com.medicall.common.security.JwtTokenProvider;
import com.medicall.domain.patient.PatientService;
import com.medicall.domain.patient.dto.PatientDetailResult;

/**
 * 개발용 우회 로그인.
 * 인증 없이 임의 환자의 토큰을 발급하므로 medicall.dev-login.enabled=true 인 환경에서만 등록된다.
 * 설정이 없으면 빈이 만들어지지 않아 엔드포인트 자체가 존재하지 않는다.(404)
 */
@RestController
@RequestMapping("api/v1/patient/auth")
@ConditionalOnProperty(name = "medicall.dev-login.enabled", havingValue = "true")
@Tag(name = "Auth", description = "로그인 API")
public class PatientDevLoginController {

    private static final Logger log = LoggerFactory.getLogger(PatientDevLoginController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final PatientService patientService;

    public PatientDevLoginController(JwtTokenProvider jwtTokenProvider, PatientService patientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.patientService = patientService;
    }

    @Operation(
            summary = "[개발 전용] 우회 로그인",
            description = "인증 없이 환자 id만으로 access token을 발급합니다. 운영 환경에서는 비활성화됩니다."
    )
    @PostMapping("/dev-login")
    public DevLoginResponse devLogin(@RequestParam Long patientId) {
        PatientDetailResult result = patientService.findById(patientId);

        String accessToken = jwtTokenProvider.generateAccessToken(patientId, "patient");

        log.warn("개발용 우회 로그인 사용 - patientId: {}", patientId);

        return new DevLoginResponse(patientId, result.name(), accessToken);
    }
}
