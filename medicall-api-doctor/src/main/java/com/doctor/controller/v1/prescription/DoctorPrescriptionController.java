package com.doctor.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.prescription.dto.request.CreateDoctorPrescriptionRequest;
import com.doctor.controller.v1.prescription.dto.response.CreateDoctorPrescriptionResponse;
import com.doctor.controller.v1.prescription.dto.response.DoctorPrescriptionDetailResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.prescription.PrescriptionService;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

@RestController
@RequestMapping("api/v1/doctor/prescriptions")
public class DoctorPrescriptionController {

    private final PrescriptionService prescriptionService;

    public DoctorPrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
    /**
     * 처방전 발행
     */
    @PostMapping
    public ResponseEntity<CreateDoctorPrescriptionResponse> createPrescription(@RequestBody CreateDoctorPrescriptionRequest request,
                                                                               @Parameter(hidden = true) CurrentUser currentUser) {
        return null;
    }

    /**
     * 처방전 단건 조회
     */
    @GetMapping("{prescriptionId}")
    public DoctorPrescriptionDetailResponse getDoctorPrescriptionDetailById(@PathVariable Long prescriptionId,
                                                                            @Parameter(hidden = true) CurrentUser currentUser) {
        PrescriptionDetailResult result = prescriptionService.getPrescriptionByDoctor(prescriptionId, currentUser.userId());

        return DoctorPrescriptionDetailResponse.from(result);
    }
}
