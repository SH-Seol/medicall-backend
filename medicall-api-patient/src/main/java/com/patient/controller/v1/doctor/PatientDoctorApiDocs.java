package com.patient.controller.v1.doctor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.medicall.common.support.CurrentUser;
import com.patient.controller.v1.doctor.dto.response.AvailableSlotResponse;
import com.patient.controller.v1.doctor.dto.response.PatientDoctorResponse;

@Tag(name = "Doctor", description = "환자 의사 관련 API")
public interface PatientDoctorApiDocs {

    @Operation(
            summary = "환자 의사 정보 조회",
            description = "환자가 의사 id를 통해 정보를 요청합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "404", description = "의사를 찾을 수 없음")
    })
    PatientDoctorResponse getDoctor(@PathVariable("doctorId") Long doctorId,
                                    @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(
            summary = "의사 예약 가능 시간 조회",
            description = "해당 날짜의 1시간 단위 슬롯을 반환합니다. 병원 운영 시간 안에서 만들어지며 "
                    + "휴게 시간·기존 예약·지난 시간은 available=false로 표시됩니다. "
                    + "휴진일이거나 운영 시간이 등록되지 않은 병원이면 빈 배열입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "소속 병원이 없는 의사"),
            @ApiResponse(responseCode = "404", description = "의사를 찾을 수 없음")
    })
    List<AvailableSlotResponse> getAvailableSlots(@PathVariable("doctorId") Long doctorId,
                                                  LocalDate date,
                                                  @Parameter(hidden = true) CurrentUser currentUser);
}
