package com.patient.security;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.security.DevLoginResponse;
import com.medicall.common.security.JwtTokenProvider;
import com.medicall.domain.patient.PatientService;
import com.medicall.domain.patient.dto.PatientDetailResult;

@RestController
@RequestMapping("api/v1/patient/auth")
@Tag(name = "Auth", description = "로그인 API")
public class PatientAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PatientService patientService;

    public PatientAuthController(JwtTokenProvider jwtTokenProvider,
                                PatientService patientService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.patientService = patientService;
    }

    @PostMapping("/dev-login")
    public DevLoginResponse devLogin(@RequestParam Long patientId) {
        PatientDetailResult result = patientService.findById(patientId);

        String accessToken = jwtTokenProvider.generateAccessToken(patientId, "patient");

        return new DevLoginResponse(patientId, result.name(), accessToken);
    }

}

