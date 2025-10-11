package com.patient.controller.v1.prescription.dto.response;

public record PatientPrescriptionQrResponse(
        String url,
        String qrImageBase64
) {
}
