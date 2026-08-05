package com.patient.controller.v1.prescription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.domain.prescription.PrescriptionService;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;
import com.patient.controller.v1.prescription.dto.response.PatientPrescriptionDetailResponse;

/**
 * 약국이 처방전 QR을 스캔했을 때 사용하는 엔드포인트.
 * 약국은 우리 서비스의 로그인 주체가 아니므로 QR 토큰 자체가 인증 수단이다.
 * 토큰은 5분간 유효하고 한 번 사용하면 폐기된다.
 */
@RestController
@RequestMapping("api/v1/prescriptions/qr")
@Tag(name = "Prescription", description = "처방전 QR 조회 API")
public class PrescriptionQrController {

    private final PrescriptionService prescriptionService;

    public PrescriptionQrController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @Operation(
            summary = "QR 토큰으로 처방전 조회",
            description = "약국이 QR을 스캔해 처방전을 조회합니다. 토큰은 1회용이며 5분간 유효합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "만료되었거나 이미 사용된 QR")
    })
    @GetMapping("/{qrToken}")
    public PatientPrescriptionDetailResponse getPrescriptionByQrToken(@PathVariable("qrToken") String qrToken) {
        PrescriptionDetailResult result = prescriptionService.getPrescriptionByQrToken(qrToken);

        return PatientPrescriptionDetailResponse.from(result);
    }
}
