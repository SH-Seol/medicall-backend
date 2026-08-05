package com.patient.security;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.security.AuthTokenController;
import com.medicall.common.security.CookieManager;
import com.medicall.common.security.TokenService;

@RestController
@RequestMapping("api/v1/patient/auth")
@Tag(name = "Auth", description = "로그인 API")
public class PatientAuthController extends AuthTokenController {

    public PatientAuthController(TokenService tokenService, CookieManager cookieManager) {
        super(tokenService, cookieManager);
    }
}
