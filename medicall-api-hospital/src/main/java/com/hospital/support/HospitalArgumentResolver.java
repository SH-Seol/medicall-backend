package com.hospital.support;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.medicall.common.security.CustomUserDetails;
import com.medicall.common.security.error.AuthErrorType;
import com.medicall.common.security.error.AuthException;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.hospital.Hospital;
import com.medicall.domain.hospital.HospitalReader;

@Component
public class HospitalArgumentResolver implements HandlerMethodArgumentResolver {

    private final HospitalReader hospitalReader;

    public HospitalArgumentResolver(HospitalReader hospitalReader) {
        this.hospitalReader = hospitalReader;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == CurrentUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException(AuthErrorType.ACCESS_DENIED);
        }

        if(!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthException(AuthErrorType.INVALID_TOKEN);
        }
        if(!userDetails.isDoctor()){
            throw new AuthException(AuthErrorType.SERVICE_TYPE_MISMATCH);
        }

        Hospital hospital = hospitalReader.findById(userDetails.userId());

        return new CurrentUser(
                hospital.id(),
                hospital.name(),
                "hospital"
        );
    }
}
