package com.doctor.security;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.doctor.config.DoctorRedirectProperties;
import com.medicall.common.security.CookieManager;
import com.medicall.common.security.JwtTokenProvider;
import com.medicall.common.security.OAuthUserInfo;
import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorReader;
import com.medicall.domain.doctor.DoctorWriter;

@Component
public class DoctorOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(DoctorOAuthSuccessHandler.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final DoctorReader doctorReader;
    private final DoctorWriter doctorWriter;
    private final CookieManager cookieManager;
    private final DoctorRedirectProperties doctorRedirectProperties;

    public DoctorOAuthSuccessHandler(JwtTokenProvider jwtTokenProvider,
                                     DoctorReader doctorReader,
                                     DoctorWriter doctorWriter,
                                     CookieManager cookieManager,
                                     DoctorRedirectProperties doctorRedirectProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.doctorReader = doctorReader;
        this.doctorWriter = doctorWriter;
        this.cookieManager = cookieManager;
        this.doctorRedirectProperties = doctorRedirectProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try{
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            OAuthUserInfo userInfo = OAuthUserInfo.from(oAuth2User.getAttributes(), registrationId);

            Doctor doctor = findOrCreateDoctor(userInfo);

            String accessToken = jwtTokenProvider.generateAccessToken(doctor.id(), "doctor");
            String refreshToken = jwtTokenProvider.generateRefreshToken(doctor.id());

            response.addCookie(cookieManager.createAccessTokenCookie(accessToken));
            response.addCookie(cookieManager.createRefreshTokenCookie(refreshToken));

            String redirectUrl = determineRedirectUri(doctor);

            log.info("의사 로그인 성공. id = {}, redirect url: {}", doctor.id(), redirectUrl);

            response.sendRedirect(redirectUrl);

        }catch(Exception e){
            log.warn("소셜 로그인 후 리다이렉트 실패", e);
            throw new AuthException(AuthErrorType.AUTHORIZATION_FAILED);
        }

    }

    private Doctor findOrCreateDoctor(OAuthUserInfo oAuthUserInfo) {
        Optional<Doctor> existingDoctor = doctorReader.findByOAuthInfo(
                oAuthUserInfo.oauthId(),
                oAuthUserInfo.provider()
        );

        return existingDoctor.orElseGet(() -> doctorWriter.createDoctor(new Doctor(
                oAuthUserInfo.name(),
                oAuthUserInfo.profileImageUrl(),
                oAuthUserInfo.oauthId(),
                oAuthUserInfo.provider()
        )));
    }

    private String determineRedirectUri(Doctor doctor) {
        return doctor.isProfileComplete() ?
                doctorRedirectProperties.dashboardUrl() :
                doctorRedirectProperties.profileCompleteUrl();
    }

}
