package com.patient.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.prescription.PrescriptionService;
import com.patient.controller.v1.prescription.dto.request.PatientPrescriptionListRequest;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionDetailResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionListResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionQrResponse;

@RestController
@RequestMapping("/api/v1/patient/prescriptions")
public class PatientPrescriptionController {

    private final PrescriptionService prescriptionService;

    public PatientPrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping("/{prescriptionId}")
    public PatientPrescriptionDetailResponse getPrescriptionDetail(@PathVariable("prescriptionId") Long prescriptionId,
                                                                   @Parameter(hidden = true) CurrentUser currentUser) {
        return null;
    }

    @GetMapping
    public PatientPrescriptionListResponse getPrescriptionList(@Parameter(hidden = true) CurrentUser currentUser,
                                                               @Valid PatientPrescriptionListRequest request) {
        return null;
    }

    @GetMapping("/{prescriptionId}/qr")
    public PatientPrescriptionQrResponse getPrescriptionQrCode(@PathVariable("prescriptionId") Long prescriptionId,
                                                               @Parameter(hidden = true) CurrentUser currentUser){
        return null;
    }
}
