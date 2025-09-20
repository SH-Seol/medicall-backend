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
    public static OAuthUserInfo fromKakao(Map<String, Object> oAuth2UserAttributes){
        Long oauthId = (Long) oAuth2UserAttributes.get("oauth_id");
        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2UserAttributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new OAuthUserInfo(
                String.valueOf(oauthId),
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname"),
                (String) profile.get("profile_image_url"),
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
