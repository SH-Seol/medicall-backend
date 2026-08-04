package com.medicall.common.security;

import java.util.Map;

import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;

public record OAuthUserInfo(
        String oauthId,
        String email,
        String name,
        String profileImageUrl,
        String provider
) {
    @SuppressWarnings("unchecked")
    public static OAuthUserInfo fromKakao(Map<String, Object> oAuth2UserAttributes){
        // 카카오는 사용자 식별자를 "id"로 내려준다. (application.yml의 user-name-attribute와 동일)
        Object oauthId = oAuth2UserAttributes.get("id");
        if(oauthId == null){
            throw new AuthException(AuthErrorType.OAUTH_USER_INFO_MISSING);
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2UserAttributes.get("kakao_account");
        if(kakaoAccount == null){
            throw new AuthException(AuthErrorType.OAUTH_USER_INFO_MISSING);
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new OAuthUserInfo(
                String.valueOf(oauthId),
                (String) kakaoAccount.get("email"),
                profile != null ? (String) profile.get("nickname") : null,
                profile != null ? (String) profile.get("profile_image_url") : null,
                "kakao"
        );
    }

    public static OAuthUserInfo from(Map<String, Object> oAuth2UserAttributes, String registrationId){
        return switch(registrationId.toLowerCase()){
            case "kakao" -> fromKakao(oAuth2UserAttributes);
            default -> throw new AuthException(AuthErrorType.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }
}
