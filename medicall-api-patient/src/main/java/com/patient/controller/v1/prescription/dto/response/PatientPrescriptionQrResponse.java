package com.patient.controller.v1.prescription.dto.response;

public record PatientPrescriptionQrResponse(
        String url,
        String qrImageBase64
) {
    public static PatientPrescriptionQrResponse of(String url, String qrImageBase64) {
        return new PatientPrescriptionQrResponse(url, qrImageBase64);
    }
}
