package com.patient.support;

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
import com.medicall.domain.patient.PatientReader;
import com.medicall.domain.patient.dto.PatientDetailResult;

@Component
public class PatientArgumentResolver implements HandlerMethodArgumentResolver {

    private final PatientReader patientReader;

    public PatientArgumentResolver(PatientReader patientReader) {
        this.patientReader = patientReader;
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
        if(!userDetails.isPatient()){
            throw new AuthException(AuthErrorType.SERVICE_TYPE_MISMATCH);
        }

        PatientDetailResult patient = patientReader.findById(userDetails.userId());

        return new CurrentUser(
                patient.id(),
                patient.name(),
                "patient"
        );
    }
}
