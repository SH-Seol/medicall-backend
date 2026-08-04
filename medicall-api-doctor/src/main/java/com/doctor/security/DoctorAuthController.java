package com.doctor.security;


import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.security.AuthTokenController;
import com.medicall.common.security.CookieManager;
import com.medicall.common.security.DevLoginResponse;
import com.medicall.common.security.JwtTokenProvider;
import com.medicall.common.security.TokenService;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;

@RestController
@RequestMapping("api/v1/doctor/auth")
@Tag(name = "Auth", description = "로그인 API")
public class DoctorAuthController extends AuthTokenController {

    private final JwtTokenProvider jwtTokenProvider;
    private final DoctorService doctorService;

    public DoctorAuthController(JwtTokenProvider jwtTokenProvider,
                                DoctorService doctorService,
                                TokenService tokenService,
                                CookieManager cookieManager) {
        super(tokenService, cookieManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.doctorService = doctorService;
    }

    @PostMapping("/dev-login")
    public DevLoginResponse devLogin(@RequestParam Long doctorId) {
        DoctorResult result = doctorService.findById(doctorId);

        String accessToken = jwtTokenProvider.generateAccessToken(doctorId, "doctor");

        return new DevLoginResponse(doctorId, result.name(), accessToken);
    }

}
