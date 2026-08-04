package com.medicall.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.medicall.common.dto.ErrorResponse;
import com.medicall.common.security.error.AuthException;
import com.medicall.error.CoreErrorKind;
import com.medicall.error.CoreException;

/**
 * 세 서비스(api-patient/doctor/hospital)가 공유하는 전역 예외 처리.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handleCoreException(CoreException e) {
        HttpStatus status = toHttpStatus(e.getErrorType().getErrorKind());

        if (status.is5xxServerError()) {
            log.error("[{}] {}", e.getErrorType().getErrorCode().getCode(), e.getMessage(), e);
        } else {
            log.warn("[{}] {}", e.getErrorType().getErrorCode().getCode(), e.getMessage());
        }

        return ResponseEntity.status(status).body(ErrorResponse.of(
                e.getErrorType().getErrorCode().getCode(),
                e.getMessage(),
                e.getData()
        ));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        log.warn("[{}] {}", e.getAuthErrorType().getAuthErrorCode().getErrorCode(), e.getMessage());

        return ResponseEntity.status(e.getAuthErrorType().getHttpStatus()).body(ErrorResponse.of(
                e.getAuthErrorType().getAuthErrorCode().getErrorCode(),
                e.getMessage(),
                e.getData()
        ));
    }

    /**
     * @Valid 검증 실패 — 어떤 필드가 왜 틀렸는지 함께 내려준다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(ErrorResponse.of(
                "COMMON-400",
                "요청 값이 올바르지 않습니다.",
                fieldErrors
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("처리되지 않은 예외", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(
                "COMMON-500",
                "서버 오류가 발생했습니다."
        ));
    }

    private HttpStatus toHttpStatus(CoreErrorKind errorKind) {
        return switch (errorKind) {
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
