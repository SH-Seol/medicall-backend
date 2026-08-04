package com.hospital.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import com.hospital.controller.department.dto.response.HospitalDepartmentResponse;

@Tag(name = "Department", description = "진료과 조회 API (온보딩용)")
public interface HospitalDepartmentApiDocs {

    @Operation(
            summary = "진료과 목록 조회",
            description = "병원 온보딩에서 선택할 수 있는 진료과 전체 목록입니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    List<HospitalDepartmentResponse> getDepartments();
}
