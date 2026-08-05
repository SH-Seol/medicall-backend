package com.doctor.controller.v1.hospital;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PathVariable;

import com.doctor.controller.v1.hospital.dto.response.DoctorInvitationResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Hospital", description = "의사 병원 소속 API")
public interface DoctorHospitalApiDocs {

    @Operation(
            summary = "초대 코드 확인",
            description = "초대 링크로 접속했을 때 어떤 병원의 초대인지 확인합니다. 수락 전 미리보기용입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대 코드"),
            @ApiResponse(responseCode = "409", description = "이미 사용되었거나 만료된 초대"),
            @ApiResponse(responseCode = "429", description = "시도 횟수 초과")
    })
    DoctorInvitationResponse getInvitation(@PathVariable("code") String code,
                                           @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "초대 수락",
            description = "초대를 수락해 해당 병원에 소속됩니다. 이미 소속된 병원이 있으면 실패합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "이미 소속된 병원이 있음"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 초대 코드"),
            @ApiResponse(responseCode = "409", description = "이미 사용되었거나 만료된 초대")
    })
    DoctorInvitationResponse acceptInvitation(@PathVariable("code") String code,
                                              @Parameter(hidden = true) CurrentUser currentUser);
}
