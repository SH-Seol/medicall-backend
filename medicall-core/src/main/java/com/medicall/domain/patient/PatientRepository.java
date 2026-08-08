package com.medicall.domain.patient;

import java.util.List;
import java.util.Optional;

import com.medicall.domain.address.Address;

import com.medicall.domain.patient.dto.PatientProfileUpdate;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository {
    Optional<Patient> findById(Long patientId);
    Patient create(NewPatient newPatient);
    Optional<Patient> findByOAuthInfo(String oauthId, String provider);
    Patient updateProfile(Long patientId, PatientProfileUpdate profileUpdate);

    List<Address> findAddresses(Long patientId);
    Address addAddress(Long patientId, Address address);
    Address updateAddress(Long patientId, Long addressId, Address address);
    void deleteAddress(Long patientId, Long addressId);
    void changeDefaultAddress(Long patientId, Long addressId);
}
