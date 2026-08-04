package com.doctor.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.doctor.dto.request.UpdateDoctorProfileRequest;
import com.doctor.controller.v1.doctor.dto.response.DoctorProfileResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;

@RestController
@RequestMapping("api/v1/doctor/me")
public class DoctorController implements DoctorApiDocs {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public DoctorProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser) {
        DoctorResult result = doctorService.findById(currentUser.userId());

        return DoctorProfileResponse.from(result);
    }

    @PatchMapping
    public DoctorProfileResponse updateMyProfile(@Valid @RequestBody UpdateDoctorProfileRequest request,
                                                 @Parameter(hidden = true) CurrentUser currentUser) {
        DoctorResult result = doctorService.updateMyProfile(currentUser.userId(), request.toProfileUpdate());

        return DoctorProfileResponse.from(result);
    }
}
