package com.patient.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.prescription.PrescriptionService;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;
import com.medicall.domain.prescription.dto.PrescriptionListResult;
import com.medicall.support.CursorPageResult;
import com.patient.controller.v1.prescription.dto.request.PatientPrescriptionListRequest;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionDetailResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionListResponse;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionQrResponse;
import com.patient.support.QrGenerator;

@RestController
@RequestMapping("/api/v1/patient/prescriptions")
public class PatientPrescriptionController {

    private final PrescriptionService prescriptionService;
    private final QrGenerator qrGenerator;

    public PatientPrescriptionController(PrescriptionService prescriptionService,
                                         QrGenerator qrGenerator) {
        this.prescriptionService = prescriptionService;
        this.qrGenerator = qrGenerator;
    }

    @GetMapping("/{prescriptionId}")
    public PatientPrescriptionDetailResponse getPrescriptionDetail(@PathVariable("prescriptionId") Long prescriptionId,
                                                                   @Parameter(hidden = true) CurrentUser currentUser) {
        PrescriptionDetailResult result = prescriptionService.getPrescriptionByPatient(prescriptionId,
                currentUser.userId());

        return PatientPrescriptionDetailResponse.from(result);
    }

    @GetMapping("/{prescriptionId}/qr")
    public PatientPrescriptionQrResponse getPrescriptionQrCode(@PathVariable("prescriptionId") Long prescriptionId,
                                                               @Parameter(hidden = true) CurrentUser currentUser){

        String qrToken = prescriptionService.generatePrescriptionQrToken(prescriptionId, currentUser.userId());
        String qrImage = qrGenerator.generateQrCodeImage(qrToken);

        return null;
    }
}
