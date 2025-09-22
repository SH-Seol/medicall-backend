package com.medicall.error;

public enum CoreErrorCode {
    HOSPITAL001("HOSPITAL-001"),
    PATIENT001("PATIENT-001"),
    DOCTOR001("DOCTOR-001"),
    DOCTOR002("DOCTOR-002"),
    DOCTOR003("DOCTOR-003"),
    DEPARTMENT001("DEPARTMENT-001"),
    APPOINTMENT001("APPOINTMENT-001"),
    APPOINTMENT002("APPOINTMENT-002"),
    TREATMENT001("TREATMENT-001"),
    TREATMENT002("TREATMENT-002"),
    MEDICINE001("MEDICINE-001"),
    PRESENTATION001("PRESENTATION-001"),
    ;
    private final String code;

    CoreErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
