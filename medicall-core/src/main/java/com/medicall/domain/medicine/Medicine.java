package com.medicall.domain.medicine;

public record Medicine(
        Long id,
        String name,
        String manufacturer,
        String unit
) {
}
