package com.patient.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorService;
import com.medicall.domain.doctor.dto.DoctorResult;
import com.patient.controller.v1.doctor.dto.response.PatientDoctorResponse;

@RestController
@RequestMapping("api/v1/patient/doctors")
public class PatientDoctorController implements PatientDoctorApiDocs{

    private final DoctorService doctorService;

    public PatientDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{doctorId}")
    public PatientDoctorResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                           @Parameter(hidden = true) CurrentUser currentUser) {
        DoctorResult doctor = doctorService.findById(doctorId);
        return PatientDoctorResponse.from(doctor);
    }
}
