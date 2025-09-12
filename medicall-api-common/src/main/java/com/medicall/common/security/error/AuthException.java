package com.medicall.common.security.error;


import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {

    private final AuthErrorType authErrorType;
    private final Object data;

    public AuthException(AuthErrorType authErrorType) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = null;
    }

    public AuthException(AuthErrorType authErrorType, Object data) {
        super(authErrorType.getMessage());
        this.authErrorType = authErrorType;
        this.data = data;
    }

    public AuthErrorType getAuthErrorType() {
        return authErrorType;
    }

    public Object getData() {
        return data;
    }
}
