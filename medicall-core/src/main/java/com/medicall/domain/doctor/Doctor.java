package com.medicall.domain.doctor;

import com.medicall.domain.department.Department;
import com.medicall.domain.hospital.Hospital;

public record Doctor(
        Long id,
        String name,
        Hospital hospital,
        String introduction,
        String imageUrl,
        Department department
) {
    public Doctor(String name, String introduction, Hospital hospital, String imageUrl, Department department){
        this(null, name, hospital, introduction, imageUrl, department);
    }
}
