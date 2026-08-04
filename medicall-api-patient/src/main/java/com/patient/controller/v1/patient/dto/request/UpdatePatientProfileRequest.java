package com.patient.controller.v1.patient.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.patient.ContactPerson;
import com.medicall.domain.patient.dto.PatientProfileUpdate;

@Schema(description = "환자 내 정보 수정 요청 (null인 항목은 수정하지 않습니다.)")
public record UpdatePatientProfileRequest(
        @Schema(description = "이름", example = "홍길동")
        @Size(min = 1, max = 30)
        String name,

        @Schema(description = "성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
        @Pattern(regexp = "MALE|FEMALE", message = "성별은 MALE 또는 FEMALE 이어야 합니다.")
        String gender,

        @Schema(description = "혈액형", example = "A+")
        @Size(max = 10)
        String bloodType,

        @Schema(description = "키(cm)", example = "175.5")
        @Positive
        Double height,

        @Schema(description = "몸무게(kg)", example = "68.2")
        @Positive
        Double weight,

        @Schema(description = "생년월일", example = "1995-03-21")
        LocalDate dateOfBirth,

        @Schema(description = "프로필 이미지 url")
        String imageUrl,

        @Schema(description = "만성 질환 목록 (전달 시 기존 목록을 전체 교체)")
        List<String> chronicDiseases,

        @Schema(description = "비상 연락처")
        ContactPersonRequest emergencyContact,

        @Schema(description = "보호자 정보")
        ContactPersonRequest guardian
) {
    public PatientProfileUpdate toProfileUpdate() {
        return new PatientProfileUpdate(
                name,
                gender,
                bloodType,
                height,
                weight,
                dateOfBirth,
                imageUrl,
                chronicDiseases,
                toContactPerson(emergencyContact),
                toContactPerson(guardian)
        );
    }

    private ContactPerson toContactPerson(ContactPersonRequest request) {
        if (request == null) {
            return null;
        }
        return new ContactPerson(request.name(), request.relationship(), request.phoneNumber());
    }

    public record ContactPersonRequest(
            @Schema(description = "이름", example = "홍부모")
            @Size(max = 30)
            String name,

            @Schema(description = "관계", example = "모")
            @Size(max = 20)
            String relationship,

            @Schema(description = "연락처", example = "010-1234-5678")
            @Pattern(regexp = "^0\\d{1,2}-?\\d{3,4}-?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
            String phoneNumber
    ) {
    }
}
