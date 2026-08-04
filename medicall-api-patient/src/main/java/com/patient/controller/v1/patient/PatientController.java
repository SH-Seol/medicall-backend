package com.patient.controller.v1.patient;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.patient.PatientService;
import com.medicall.domain.patient.dto.PatientDetailResult;
import com.patient.controller.v1.patient.dto.request.UpdatePatientProfileRequest;
import com.patient.controller.v1.patient.dto.response.PatientProfileResponse;

@RestController
@RequestMapping("api/v1/patient/me")
public class PatientController implements PatientApiDocs {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public PatientProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser) {
        PatientDetailResult result = patientService.findById(currentUser.userId());

        return PatientProfileResponse.from(result);
    }

    @PatchMapping
    public PatientProfileResponse updateMyProfile(@Valid @RequestBody UpdatePatientProfileRequest request,
                                                  @Parameter(hidden = true) CurrentUser currentUser) {
        PatientDetailResult result = patientService.updateMyProfile(currentUser.userId(), request.toProfileUpdate());

        return PatientProfileResponse.from(result);
    }
}
