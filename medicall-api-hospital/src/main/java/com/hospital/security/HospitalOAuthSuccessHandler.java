package com.hospital.security;

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

import com.hospital.config.HospitalRedirectProperties;
import com.medicall.common.security.CookieManager;
import com.medicall.common.security.TokenPair;
import com.medicall.common.security.TokenService;
import com.medicall.common.security.OAuthUserInfo;
import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.HospitalService;
import com.medicall.domain.hospital.NewHospital;

@Component
public class HospitalOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(HospitalOAuthSuccessHandler.class);

    private final TokenService tokenService;
    private final HospitalService hospitalService;
    private final CookieManager cookieManager;
    private final HospitalRedirectProperties hospitalRedirectProperties;

    public HospitalOAuthSuccessHandler(TokenService tokenService, HospitalService hospitalService,
                                       CookieManager cookieManager,
                                       HospitalRedirectProperties hospitalRedirectProperties) {
        this.tokenService = tokenService;
        this.hospitalService = hospitalService;
        this.cookieManager = cookieManager;
        this.hospitalRedirectProperties = hospitalRedirectProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try{
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
            String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            OAuthUserInfo userInfo = OAuthUserInfo.from(oAuth2User.getAttributes(), registrationId);

            Hospital hospital = findOrCreateHospital(userInfo);

            TokenPair tokens = tokenService.issue(hospital.id(), "hospital");

            response.addHeader(HttpHeaders.SET_COOKIE, cookieManager.createAccessTokenCookie(tokens.accessToken()).toString());
            response.addHeader(HttpHeaders.SET_COOKIE, cookieManager.createRefreshTokenCookie(tokens.refreshToken()).toString());

            String redirectUrl = determineRedirectUri(hospital);

            log.info("병원 로그인 성공. id ={}, redirect url: {}", hospital.id(), redirectUrl);

            response.sendRedirect(redirectUrl);

        }catch (Exception e){
            log.warn("병원 소셜 로그인 후 리다이렉트 실패", e);
            throw new AuthException(AuthErrorType.AUTHORIZATION_FAILED);
        }
    }

    private Hospital findOrCreateHospital(OAuthUserInfo oAuthUserInfo) {
        return hospitalService.findOrCreateByOAuth(new NewHospital(
                oAuthUserInfo.name(),
                oAuthUserInfo.profileImageUrl(),
                oAuthUserInfo.oauthId(),
                oAuthUserInfo.provider(),
                oAuthUserInfo.email()
        ));
    }

    private String determineRedirectUri(Hospital hospital){
        return hospital.isSetUpComplete() ?
                hospitalRedirectProperties.dashboardUrl() :
                hospitalRedirectProperties.setUpUrl();
    }
}
