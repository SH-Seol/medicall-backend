package com.hospital.controller.hospital;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.controller.hospital.dto.request.UpdateHospitalAddressRequest;
import com.hospital.controller.hospital.dto.request.UpdateHospitalDepartmentsRequest;
import com.hospital.controller.hospital.dto.request.UpdateHospitalProfileRequest;
import com.hospital.controller.hospital.dto.request.UpdateOperatingTimesRequest;
import com.hospital.controller.hospital.dto.response.HospitalProfileResponse;
import com.hospital.controller.hospital.dto.response.HospitalSetupStatusResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.hospital.HospitalService;
import com.medicall.domain.hospital.dto.HospitalProfileResult;

@RestController
@RequestMapping("api/v1/hospital/mypage")
public class HospitalController implements HospitalApiDocs {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public HospitalProfileResponse getMyProfile(@Parameter(hidden = true) CurrentUser currentUser) {
        HospitalProfileResult result = hospitalService.getMyProfile(currentUser.userId());

        return HospitalProfileResponse.from(result);
    }

    @PatchMapping
    public HospitalProfileResponse updateMyProfile(@Valid @RequestBody UpdateHospitalProfileRequest request,
                                                   @Parameter(hidden = true) CurrentUser currentUser) {
        HospitalProfileResult result = hospitalService.updateMyProfile(currentUser.userId(), request.toProfileUpdate());

        return HospitalProfileResponse.from(result);
    }

    @PutMapping("/operating-times")
    public HospitalProfileResponse updateOperatingTimes(@Valid @RequestBody UpdateOperatingTimesRequest request,
                                                        @Parameter(hidden = true) CurrentUser currentUser) {
        hospitalService.updateOperatingTime(currentUser.userId(), request.toOperatingTimes());

        return HospitalProfileResponse.from(hospitalService.getMyProfile(currentUser.userId()));
    }

    @PutMapping("/address")
    public HospitalProfileResponse updateAddress(@Valid @RequestBody UpdateHospitalAddressRequest request,
                                                 @Parameter(hidden = true) CurrentUser currentUser) {
        HospitalProfileResult result = hospitalService.updateAddress(currentUser.userId(), request.toAddress());

        return HospitalProfileResponse.from(result);
    }

    @PutMapping("/departments")
    public HospitalProfileResponse updateDepartments(@Valid @RequestBody UpdateHospitalDepartmentsRequest request,
                                                     @Parameter(hidden = true) CurrentUser currentUser) {
        HospitalProfileResult result = hospitalService.updateDepartments(currentUser.userId(), request.departmentIds());

        return HospitalProfileResponse.from(result);
    }

    @GetMapping("/setup-status")
    public HospitalSetupStatusResponse getSetupStatus(@Parameter(hidden = true) CurrentUser currentUser) {
        return HospitalSetupStatusResponse.from(hospitalService.getSetupStatus(currentUser.userId()));
    }
}
