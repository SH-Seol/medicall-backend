package com.hospital.controller.appointment.dto.response;

import java.time.LocalDateTime;

public record HospitalAppointmentAcceptResponse(
        Long appointmentId,
        LocalDateTime appointmentDate
) {
}
