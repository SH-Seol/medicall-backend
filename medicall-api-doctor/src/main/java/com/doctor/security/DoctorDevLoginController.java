package com.doctor.security;

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
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;

/**
 * 개발용 우회 로그인.
 * 인증 없이 임의 의사의 토큰을 발급하므로 medicall.dev-login.enabled=true 인 환경에서만 등록된다.
 * 설정이 없으면 빈이 만들어지지 않아 엔드포인트 자체가 존재하지 않는다.(404)
 */
@RestController
@RequestMapping("api/v1/doctor/auth")
@ConditionalOnProperty(name = "medicall.dev-login.enabled", havingValue = "true")
@Tag(name = "Auth", description = "로그인 API")
public class DoctorDevLoginController {

    private static final Logger log = LoggerFactory.getLogger(DoctorDevLoginController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final DoctorService doctorService;

    public DoctorDevLoginController(JwtTokenProvider jwtTokenProvider, DoctorService doctorService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.doctorService = doctorService;
    }

    @Operation(
            summary = "[개발 전용] 우회 로그인",
            description = "인증 없이 의사 id만으로 access token을 발급합니다. 운영 환경에서는 비활성화됩니다."
    )
    @PostMapping("/dev-login")
    public DevLoginResponse devLogin(@RequestParam Long doctorId) {
        DoctorResult result = doctorService.findById(doctorId);

        String accessToken = jwtTokenProvider.generateAccessToken(doctorId, "doctor");

        log.warn("개발용 우회 로그인 사용 - doctorId: {}", doctorId);

        return new DevLoginResponse(doctorId, result.name(), accessToken);
    }
}
