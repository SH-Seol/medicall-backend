package com.hospital.security;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.security.AuthTokenController;
import com.medicall.common.security.CookieManager;
import com.medicall.common.security.TokenService;

@RestController
@RequestMapping("api/v1/hospital/auth")
@Tag(name = "Auth", description = "로그인 API")
public class HospitalAuthController extends AuthTokenController {

    public HospitalAuthController(TokenService tokenService, CookieManager cookieManager) {
        super(tokenService, cookieManager);
    }
}
