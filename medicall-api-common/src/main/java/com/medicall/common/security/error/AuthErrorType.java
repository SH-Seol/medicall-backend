package com.medicall.common.security.error;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum AuthErrorType {

    AUTHORIZATION_FAILED(AuthErrorCode.AUTH01, HttpStatus.UNAUTHORIZED, "사용자 인가에 실패하였습니다.", LogLevel.WARN),
    UNREADABLE_TOKEN(AuthErrorCode.AUTH02, HttpStatus.FORBIDDEN, "토큰 읽기에 실패하였습니다.", LogLevel.WARN),
    INVALID_TOKEN(AuthErrorCode.AUTH03, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", LogLevel.WARN),
    EXPIRED_TOKEN(AuthErrorCode.AUTH04, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", LogLevel.WARN),
    MALFORMED_TOKEN(AuthErrorCode.AUTH05, HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰입니다.", LogLevel.WARN),
    INVALID_SIGNATURE(AuthErrorCode.AUTH06, HttpStatus.UNAUTHORIZED, "토큰 서명이 올바르지 않습니다.", LogLevel.WARN),
    TOKEN_NOT_PROVIDED(AuthErrorCode.AUTH07, HttpStatus.UNAUTHORIZED, "토큰이 제공되지 않았습니다.", LogLevel.WARN),
    SAVING_REFRESH_TOKEN_FAILED(AuthErrorCode.AUTH08, HttpStatus.UNAUTHORIZED, "리프래시 토큰 저장에 실패하였습니다.", LogLevel.WARN),
    IS_BLACKED_TOKEN(AuthErrorCode.AUTH09, HttpStatus.UNAUTHORIZED, "블랙리스트에 지정된 토큰입니다.", LogLevel.WARN),
    ;

    private final AuthErrorCode authErrorCode;
    private final HttpStatus httpStatus;
    private final String message;
    private final LogLevel logLevel;

    AuthErrorType(AuthErrorCode authErrorCode, HttpStatus httpStatus, String message, LogLevel logLevel) {
        this.authErrorCode = authErrorCode;
        this.httpStatus = httpStatus;
        this.message = message;
        this.logLevel = logLevel;
    }

    public AuthErrorCode getAuthErrorCode() {
        return this.authErrorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }
}
