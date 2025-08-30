package com.medicall.storage.db.core.department;

import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.hospital.HospitalEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class HospitalDepartmentEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    protected HospitalDepartmentEntity() {}

    public HospitalDepartmentEntity(HospitalEntity hospital, DepartmentEntity department) {
        this.hospital = hospital;
        this.department = department;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }
}
