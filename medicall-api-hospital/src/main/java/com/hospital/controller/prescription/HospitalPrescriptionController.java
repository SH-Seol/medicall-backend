package com.hospital.controller.prescription;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.prescription.dto.response.HospitalPrescriptionDetailResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.prescription.PrescriptionService;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;

@RestController
@RequestMapping("api/v1/hospital/prescription")
public class HospitalPrescriptionController {

    private final PrescriptionService prescriptionService;

    public HospitalPrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * 처방전 조회
     */
    @GetMapping("/{prescriptionId}")
    public HospitalPrescriptionDetailResponse getPrescriptionById(@PathVariable("prescriptionId") Long prescriptionId,
                                                                  @Parameter(hidden = true) CurrentUser currentUser) {
        PrescriptionDetailResult result = prescriptionService.getPrescriptionByHospital(prescriptionId,
                currentUser.userId());
        return HospitalPrescriptionDetailResponse.from(result);
    }
}
