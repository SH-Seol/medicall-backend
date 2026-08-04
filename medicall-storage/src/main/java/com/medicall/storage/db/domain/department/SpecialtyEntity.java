package com.medicall.storage.db.domain.department;

import com.medicall.domain.department.Specialty;
import com.medicall.storage.db.domain.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * 세부 전공 (ex. 내과 - 심장내과)
 */
@Entity
@Table(name = "specialties",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_specialty_department_name",
                        columnNames = {"department_id", "name"}
                )
        }
)
public class SpecialtyEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    protected SpecialtyEntity() {}

    public SpecialtyEntity(String name, DepartmentEntity department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public Specialty toDomainModel() {
        return new Specialty(
                this.id,
                this.name,
                this.department.getId(),
                this.department.getName()
        );
    }
}
