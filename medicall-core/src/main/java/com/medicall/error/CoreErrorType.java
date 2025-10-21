package com.medicall.error;

public enum CoreErrorType {
    HOSPITAL_NOT_FOUND(CoreErrorCode.HOSPITAL001, CoreErrorKind.NOT_FOUND, "존재하지 않는 병원입니다.", CoreErrorLevel.WARN),
    PATIENT_NOT_FOUND(CoreErrorCode.PATIENT001, CoreErrorKind.NOT_FOUND, "존재하지 않는 환자입니다.", CoreErrorLevel.WARN),
    DOCTOR_NOT_FOUND(CoreErrorCode.DOCTOR001, CoreErrorKind.NOT_FOUND, "존재하지 않는 의사입니다.", CoreErrorLevel.WARN),
    DOCTOR_IN_TREATMENT_NOT_MATCH(CoreErrorCode.DOCTOR002, CoreErrorKind.FORBIDDEN, "진단 의사와 처방전 작성한 의사가 다른 요청입니다.", CoreErrorLevel.WARN),
    DOCTOR_BELONGS_TO_HOSPITAL(CoreErrorCode.DOCTOR003, CoreErrorKind.FORBIDDEN, "이미 소속된 병원이 있는 의사입니다.", CoreErrorLevel.WARN),
    DEPARTMENT_NOT_FOUND(CoreErrorCode.DEPARTMENT001, CoreErrorKind.NOT_FOUND, "존재하지 않는 전공입니다.", CoreErrorLevel.WARN),
    APPOINTMENT_NOT_FOUND(CoreErrorCode.APPOINTMENT001, CoreErrorKind.NOT_FOUND, "존재하지 않는 예약입니다.", CoreErrorLevel.WARN),
    APPOINTMENT_ADD_DOCTOR_FAILED(CoreErrorCode.APPOINTMENT002, CoreErrorKind.BAD_REQUEST, "해당 예약에 의사 추가를 실패하였습니다.", CoreErrorLevel.WARN),
    APPOINTMENT_EXCESS_REJECTED(CoreErrorCode.APPOINTMENT003, CoreErrorKind.FORBIDDEN, "해당 예약에 접근할 수 없습니다", CoreErrorLevel.WARN),
    APPOINTMENT_RESERVATION_TIME_IS_PAST(CoreErrorCode.APPOINTMENT004, CoreErrorKind.BAD_REQUEST, "과거 시간으로 예약할 수 없습니다.", CoreErrorLevel.WARN),
    APPOINTMENT_ALREADY_EXIST_FOR_PATIENT(CoreErrorCode.APPOINTMENT005, CoreErrorKind.CONFLICT, "예약 요청 시간에 이미 환자의 예약이 존재합니다.", CoreErrorLevel.WARN),
    APPOINTMENT_ALREADY_EXIST_FOR_DOCTOR(CoreErrorCode.APPOINTMENT006, CoreErrorKind.CONFLICT, "에약 요청 시간에 이미 의사의 예약이 존재합니다.", CoreErrorLevel.WARN),
    TREATMENT_NOT_FOUND(CoreErrorCode.TREATMENT001, CoreErrorKind.NOT_FOUND, "존재하지 않는 진료 내역입니다.", CoreErrorLevel.WARN),
    TREATMENT_ALREADY_HAS_PRESCRIPTION(CoreErrorCode.TREATMENT002, CoreErrorKind.FORBIDDEN, "해당 진료에 처방이 이미 이루어졌습니다.", CoreErrorLevel.WARN),
    TREATMENT_NOT_ACCESSIBLE(CoreErrorCode.TREATMENT003, CoreErrorKind.FORBIDDEN, "해당 진료에 접근할 수 없습니다.", CoreErrorLevel.WARN),
    MEDICINE_NOT_FOUND(CoreErrorCode.MEDICINE001, CoreErrorKind.NOT_FOUND, "의약품을 찾을 수 없습니다.", CoreErrorLevel.WARN),
    PRESCRIPTION_NOT_FOUND(CoreErrorCode.PRESCRIPTION001, CoreErrorKind.NOT_FOUND, "처방전을 찾을 수 없습니다", CoreErrorLevel.WARN),
    PRESCRIPTION_NOT_ACCESSIBLE(CoreErrorCode.PRESCRIPTION002, CoreErrorKind.FORBIDDEN, "해당 처방전을 조회할 수 없습니다.", CoreErrorLevel.WARN),
    ;
    private final CoreErrorCode errorCode;
    private final CoreErrorKind errorKind;
    private final String message;
    private final CoreErrorLevel errorLevel;

    CoreErrorType(CoreErrorCode errorCode, CoreErrorKind errorKind, String message, CoreErrorLevel errorLevel) {
        this.errorCode = errorCode;
        this.errorKind = errorKind;
        this.message = message;
        this.errorLevel = errorLevel;
    }

    public CoreErrorCode getErrorCode() {
        return errorCode;
    }

    public CoreErrorKind getErrorKind() {
        return errorKind;
    }

    public String getMessage() {
        return message;
    }

    public CoreErrorLevel getErrorLevel() {
        return errorLevel;
    }
}
