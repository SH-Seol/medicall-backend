package com.doctor.controller.v1.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.doctor.controller.v1.department.dto.response.DepartmentResponse;
import com.doctor.controller.v1.department.dto.response.SpecialtyResponse;

@Tag(name = "Department", description = "진료과·세부전공 조회 API (온보딩용)")
public interface DoctorDepartmentApiDocs {

    @Operation(
            summary = "진료과 목록 조회",
            description = "온보딩에서 선택할 수 있는 진료과 전체 목록입니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    List<DepartmentResponse> getDepartments();

    @Operation(
            summary = "세부 전공 목록 조회",
            description = "선택한 진료과에 속한 세부 전공 목록입니다. (ex. 내과 → 심장내과, 소화기내과)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "진료과를 찾을 수 없음")
    })
    List<SpecialtyResponse> getSpecialties(@PathVariable("departmentId") Long departmentId);
}
