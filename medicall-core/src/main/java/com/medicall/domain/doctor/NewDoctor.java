package com.medicall.domain.doctor;

public record NewDoctor(
        String name,
        String imageUrl,
        Long departmentId,
        String introduction
) {
}
