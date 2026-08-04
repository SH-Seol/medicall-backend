package com.medicall.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public record ErrorResponse(
        @Schema(description = "에러 코드", example = "SPECIALTY-002")
        String code,

        @Schema(description = "에러 메시지", example = "선택한 진료과에 속하지 않는 세부 전공입니다.")
        String message,

        @Schema(description = "부가 정보")
        Object data
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, null);
    }

    public static ErrorResponse of(String code, String message, Object data) {
        return new ErrorResponse(code, message, data);
    }
}
