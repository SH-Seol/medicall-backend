package com.medicall.domain.appointment;

import java.time.LocalDateTime;

import com.medicall.domain.address.Address;

public record NewAppointment(
        String symptom,
        Long doctorId,
        Long hospitalId,
        LocalDateTime reservationTime,
        Address address
) {
}
