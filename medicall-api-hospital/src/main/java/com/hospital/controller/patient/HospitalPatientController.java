package com.hospital.controller.patient;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.patient.dto.response.HospitalPatientDetailResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.patient.PatientService;
import com.medicall.domain.patient.dto.PatientDetailResult;

@RestController
@RequestMapping("api/v1/hospital/patient")
public class HospitalPatientController {

    private final PatientService patientService;

    public HospitalPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("{patientId}")
    public HospitalPatientDetailResponse getPatientDetail(@PathVariable Long patientId,
                                                          @Parameter(hidden = true)CurrentUser currentUser) {

        PatientDetailResult result = patientService.findById(patientId);

        return HospitalPatientDetailResponse.from(result);
    }
}
