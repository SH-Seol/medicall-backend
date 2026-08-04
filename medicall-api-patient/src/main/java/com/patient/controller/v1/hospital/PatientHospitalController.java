package com.patient.controller.v1.hospital;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.hospital.HospitalService;
import com.medicall.domain.hospital.dto.HospitalDetailResult;
import com.medicall.domain.hospital.dto.HospitalSearchResult;
import com.medicall.support.CursorPageResult;
import com.patient.controller.v1.doctor.dto.response.PatientDoctorResponse;
import com.patient.controller.v1.hospital.dto.request.PatientHospitalDetailRequest;
import com.patient.controller.v1.hospital.dto.request.PatientHospitalSearchRequest;
import com.patient.controller.v1.hospital.dto.response.PatientHospitalDetailResponse;
import com.patient.controller.v1.hospital.dto.response.PatientHospitalListResponse;

@RestController
@RequestMapping("api/v1/patient/hospitals")
public class PatientHospitalController implements PatientHospitalApiDocs{

    private final HospitalService hospitalService;
    private final DoctorService doctorService;

    public PatientHospitalController(HospitalService hospitalService, DoctorService doctorService) {
        this.hospitalService = hospitalService;
        this.doctorService = doctorService;
    }

    /**
     * 병원 소속 의사 목록 (병원 상세에서 의사를 골라 예약하는 흐름)
     */
    @GetMapping("/{hospitalId}/doctors")
    public List<PatientDoctorResponse> getHospitalDoctors(@PathVariable("hospitalId") Long hospitalId,
                                                          @Parameter(hidden = true) CurrentUser currentUser) {
        return doctorService.getDoctorsByHospital(hospitalId).stream()
                .map(PatientDoctorResponse::from)
                .toList();
    }

    @GetMapping("/nearby")
    public CursorPageResponse<PatientHospitalListResponse> findByHospitalName(@Parameter(hidden = true) CurrentUser currentUser,
                                                                              @Valid PatientHospitalSearchRequest request) {
        CursorPageResult<HospitalSearchResult> result = hospitalService.getHospitalsNearby(request.toCriteria(currentUser.userId()));

        List<PatientHospitalListResponse> hospitalListResponses = result.data().stream().map(PatientHospitalListResponse::from).toList();

        return CursorPageResponse.of(hospitalListResponses, request.cursorId(), result.nextCursorId());
    }

    @GetMapping("/{hospitalId}")
    public PatientHospitalDetailResponse getHospitalDetail(@PathVariable("hospitalId") Long hospitalId,
                                                           @Valid PatientHospitalDetailRequest request,
                                                           @Parameter(hidden = true) CurrentUser currentUser) {

        HospitalDetailResult result = hospitalService.findById(hospitalId, request.latitude(), request.longitude());

        return PatientHospitalDetailResponse.from(result);
    }
}
