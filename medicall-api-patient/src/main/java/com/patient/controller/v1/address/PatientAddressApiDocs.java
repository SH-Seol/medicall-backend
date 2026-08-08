package com.patient.controller.v1.address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.medicall.common.support.CurrentUser;
import com.patient.controller.v1.address.dto.request.PatientAddressRequest;
import com.patient.controller.v1.address.dto.response.PatientAddressResponse;

@Tag(name = "Address", description = "환자 주소 API")
public interface PatientAddressApiDocs {

    @Operation(summary = "주소 목록 조회", description = "저장된 방문 주소 목록입니다. 예약 시 선택해 사용합니다.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "성공"))
    List<PatientAddressResponse> getAddresses(@Parameter(hidden = true) CurrentUser currentUser);

    @Operation(summary = "주소 등록", description = "첫 주소는 자동으로 기본 주소가 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터")
    })
    ResponseEntity<PatientAddressResponse> addAddress(PatientAddressRequest request,
                                                      @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(summary = "주소 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음")
    })
    PatientAddressResponse updateAddress(@PathVariable("addressId") Long addressId,
                                         PatientAddressRequest request,
                                         @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(summary = "주소 삭제", description = "기본 주소를 삭제하면 남은 주소 중 하나가 기본으로 지정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음")
    })
    ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Long addressId,
                                       @Parameter(hidden = true) CurrentUser currentUser);

    @Operation(summary = "기본 주소 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없음")
    })
    ResponseEntity<Void> changeDefaultAddress(@PathVariable("addressId") Long addressId,
                                              @Parameter(hidden = true) CurrentUser currentUser);
}
