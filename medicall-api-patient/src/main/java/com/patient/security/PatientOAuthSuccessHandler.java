package com.patient.security;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.medicall.common.security.CookieManager;
import com.medicall.common.security.TokenPair;
import com.medicall.common.security.TokenService;
import com.medicall.common.security.OAuthUserInfo;
import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;
import com.medicall.domain.patient.NewPatient;
import com.medicall.domain.patient.Patient;
import com.medicall.domain.patient.PatientService;
import com.patient.config.PatientRedirectProperties;

@Component
public class PatientOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(PatientOAuthSuccessHandler.class);

    private final TokenService tokenService;
    private final PatientService patientService;
    private final CookieManager cookieManager;
    private final PatientRedirectProperties patientRedirectProperties;

    public PatientOAuthSuccessHandler(TokenService tokenService,
                                      PatientService patientService,
                                      CookieManager cookieManager,
                                      PatientRedirectProperties patientRedirectProperties) {
        this.tokenService = tokenService;
        this.patientService = patientService;
        this.cookieManager = cookieManager;
        this.patientRedirectProperties = patientRedirectProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try{
            OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauth2AuthenticationToken.getPrincipal();
            String registrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

            OAuthUserInfo userInfo = OAuthUserInfo.from(oauth2User.getAttributes(), registrationId);

            Patient patient = findOrCreatePatient(userInfo);

            TokenPair tokens = tokenService.issue(patient.id(), "patient");

            response.addHeader(HttpHeaders.SET_COOKIE, cookieManager.createAccessTokenCookie(tokens.accessToken()).toString());
            response.addHeader(HttpHeaders.SET_COOKIE, cookieManager.createRefreshTokenCookie(tokens.refreshToken()).toString());

            String redirectUrl = determineRedirectUrl(patient);

            log.info("환자 로그인 성공. id = {}, redirect url = {}", patient.id(), redirectUrl);

            response.sendRedirect(redirectUrl);
        }catch (Exception e) {
            log.warn("환자 소셜 로그인 후 리다이렉트 실패", e);
            throw new AuthException(AuthErrorType.AUTHORIZATION_FAILED);
        }
    }

    private Patient findOrCreatePatient(OAuthUserInfo oAuthUserInfo) {
        return patientService.findOrCreateByOAuth(new NewPatient(
                oAuthUserInfo.name(),
                oAuthUserInfo.profileImageUrl(),
                oAuthUserInfo.email(),
                oAuthUserInfo.oauthId(),
                oAuthUserInfo.provider()
        ));
    }

    private String determineRedirectUrl(Patient patient) {
        return patient.isProfileComplete() ?
                patientRedirectProperties.dashboardUrl() :
                patientRedirectProperties.termsUrl();
    }
}
