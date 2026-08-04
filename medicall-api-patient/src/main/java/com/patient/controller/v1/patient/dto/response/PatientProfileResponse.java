package com.patient.controller.v1.patient.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

import com.medicall.domain.patient.ContactPerson;
import com.medicall.domain.patient.dto.PatientDetailResult;

@Schema(description = "환자 내 정보 응답")
public record PatientProfileResponse(
        Long id,
        String name,
        String gender,
        String bloodType,
        Double height,
        Double weight,
        int age,
        LocalDate dateOfBirth,
        String imageUrl,
        String email,
        List<String> chronicDiseases,
        ContactPersonResponse emergencyContact,
        ContactPersonResponse guardian
) {
    public static PatientProfileResponse from(PatientDetailResult result) {
        return new PatientProfileResponse(
                result.id(),
                result.name(),
                result.gender(),
                result.bloodType(),
                result.height(),
                result.weight(),
                result.age(),
                result.dateOfBirth(),
                result.imageUrl(),
                result.email(),
                result.chronicDiseases(),
                ContactPersonResponse.from(result.emergencyContact()),
                ContactPersonResponse.from(result.guardian())
        );
    }

    public record ContactPersonResponse(
            String name,
            String relationship,
            String phoneNumber
    ) {
        public static ContactPersonResponse from(ContactPerson contactPerson) {
            if (contactPerson == null || contactPerson.isEmpty()) {
                return null;
            }
            return new ContactPersonResponse(contactPerson.name(), contactPerson.relationship(), contactPerson.phoneNumber());
        }
    }
}
