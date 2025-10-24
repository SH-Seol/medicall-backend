package com.doctor.controller.v1.treatment;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.doctor.controller.v1.treatment.dto.request.CreateDoctorTreatmentRequest;
import com.doctor.controller.v1.treatment.dto.request.DoctorTreatmentListRequest;
import com.doctor.controller.v1.treatment.dto.response.CreateDoctorTreatmentResponse;
import com.doctor.controller.v1.treatment.dto.response.DoctorTreatmentDetailResponse;
import com.doctor.controller.v1.treatment.dto.response.DoctorTreatmentListResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.common.support.pagination.CursorPageResponse;

@Tag(name = "Treatment", description = "의사 진료 관련 API")
public interface DoctorTreatmentApiDocs {

    ResponseEntity<CreateDoctorTreatmentResponse> createTreatment(@Valid @RequestBody CreateDoctorTreatmentRequest createDoctorTreatmentRequest,
                                                                  @Parameter(hidden = true) CurrentUser currentUser);

    CursorPageResponse<DoctorTreatmentListResponse> getPatientTreatmentList (@PathVariable Long patientId,
                                                                             @Valid DoctorTreatmentListRequest request,
                                                                             @Parameter(hidden = true) CurrentUser currentUser);

    DoctorTreatmentDetailResponse getTreatmentDetail (@PathVariable Long treatmentId,
                                                             @Parameter(hidden = true) CurrentUser currentUser);
}
