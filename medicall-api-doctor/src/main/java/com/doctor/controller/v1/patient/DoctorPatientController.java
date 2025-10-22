package com.doctor.controller.v1.patient;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.patient.dto.response.DoctorPatientDetailResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.patient.PatientService;
import com.medicall.domain.patient.dto.PatientDetailResult;

@RestController
@RequestMapping("api/v1/doctor/patients")
public class DoctorPatientController implements DoctorPatientApiDocs{

    private final PatientService patientService;

    public DoctorPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * 환자 상세 조회
     */
    @GetMapping("{patientId}")
    public DoctorPatientDetailResponse getPatientDetailById(@PathVariable Long patientId,
                                                            @Parameter(hidden = true) CurrentUser currentUser){
        PatientDetailResult result = patientService.findById(patientId);

        return DoctorPatientDetailResponse.from(result);
    }
}
