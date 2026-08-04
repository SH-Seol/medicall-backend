package com.medicall.domain.department;

/**
 * 세부 전공 (ex. 내과 - 심장내과)
 */
public record Specialty(
        Long id,
        String name,
        Long departmentId,
        String departmentName
) {
}
