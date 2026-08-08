package com.doctor.controller.v1.medicine;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

import com.doctor.controller.v1.medicine.dto.response.DoctorMedicineResponse;
import com.medicall.common.support.CurrentUser;

@Tag(name = "Medicine", description = "의약품 API")
public interface DoctorMedicineApiDocs {

    @Operation(
            summary = "의약품 검색",
            description = "처방전 작성 시 의약품을 이름으로 검색합니다. 최대 5건을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "검색어가 비어 있음")
    })
    List<DoctorMedicineResponse> searchMedicines(
            @NotBlank(message = "검색어를 입력해주세요.") String keyword,
                                                 @Parameter(hidden = true) CurrentUser currentUser);
}
