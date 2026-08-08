package com.patient.controller.v1.address;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medicall.common.support.CurrentUser;
import com.medicall.domain.patient.PatientService;
import com.patient.controller.v1.address.dto.request.PatientAddressRequest;
import com.patient.controller.v1.address.dto.response.PatientAddressResponse;

@RestController
@RequestMapping("api/v1/patient/addresses")
public class PatientAddressController implements PatientAddressApiDocs {

    private final PatientService patientService;

    public PatientAddressController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientAddressResponse> getAddresses(@Parameter(hidden = true) CurrentUser currentUser) {
        return patientService.getAddresses(currentUser.userId()).stream()
                .map(PatientAddressResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PatientAddressResponse> addAddress(@Valid @RequestBody PatientAddressRequest request,
                                                             @Parameter(hidden = true) CurrentUser currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PatientAddressResponse.from(
                        patientService.addAddress(currentUser.userId(), request.toAddress())));
    }

    @PutMapping("/{addressId}")
    public PatientAddressResponse updateAddress(@PathVariable("addressId") Long addressId,
                                                @Valid @RequestBody PatientAddressRequest request,
                                                @Parameter(hidden = true) CurrentUser currentUser) {
        return PatientAddressResponse.from(
                patientService.updateAddress(currentUser.userId(), addressId, request.toAddress()));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("addressId") Long addressId,
                                              @Parameter(hidden = true) CurrentUser currentUser) {
        patientService.deleteAddress(currentUser.userId(), addressId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> changeDefaultAddress(@PathVariable("addressId") Long addressId,
                                                     @Parameter(hidden = true) CurrentUser currentUser) {
        patientService.changeDefaultAddress(currentUser.userId(), addressId);

        return ResponseEntity.noContent().build();
    }
}
