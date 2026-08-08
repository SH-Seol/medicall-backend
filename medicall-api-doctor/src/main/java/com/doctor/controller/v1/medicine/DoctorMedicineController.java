package com.doctor.controller.v1.medicine;

import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.controller.v1.medicine.dto.response.DoctorMedicineResponse;
import com.medicall.common.support.CurrentUser;
import com.medicall.domain.medicine.MedicineService;

@RestController
@RequestMapping("api/v1/doctor/medicines")
@Validated
public class DoctorMedicineController implements DoctorMedicineApiDocs {

    private final MedicineService medicineService;

    public DoctorMedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public List<DoctorMedicineResponse> searchMedicines(
            @RequestParam String keyword,
            @Parameter(hidden = true) CurrentUser currentUser) {

        return medicineService.getMedicineList(keyword).stream()
                .map(DoctorMedicineResponse::from)
                .toList();
    }
}
