package com.hospital.controller.doctor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospital.controller.doctor.dto.response.HospitalDoctorDetailResponse;
import com.hospital.controller.doctor.dto.response.HospitalInvitationResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Doctor", description = "병원 소속 의사 관리 API")
public interface HospitalDoctorApiDocs {

    @Operation(
            summary = "병원 소속 의사 목록 조회",
            description = "로그인한 병원에 소속된 의사 전체 목록입니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    List<HospitalDoctorDetailResponse> getDoctors(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "병원 소속 의사 조회",
            description = "소속 의사 한 명의 정보를 조회합니다. 다른 병원 소속 의사는 조회할 수 없습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "소속 의사를 찾을 수 없음")
    })
    HospitalDoctorDetailResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                           @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "의사 초대 코드 발급",
            description = "의사를 초대할 코드를 발급합니다. 발급된 코드는 7일간 유효하며 링크로 전달합니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "성공"))
    ResponseEntity<HospitalInvitationResponse> invite(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "발급한 초대 목록 조회",
            description = "병원이 발급한 초대와 각 상태(PENDING/ACCEPTED/CANCELED)를 조회합니다."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    List<HospitalInvitationResponse> getInvitations(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "초대 취소",
            description = "아직 수락되지 않은 초대를 취소합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대 코드"),
            @ApiResponse(responseCode = "409", description = "이미 사용되었거나 취소된 초대")
    })
    ResponseEntity<Void> cancelInvitation(@PathVariable("code") String code,
                                          @Parameter(hidden = true) CurrentUser currentUser);
}
