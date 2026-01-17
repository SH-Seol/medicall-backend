package com.medicall.storage.db.domain.department;

import com.medicall.domain.department.Department;
import com.medicall.storage.db.domain.common.domain.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class DepartmentEntity extends BaseEntity {
    /*
    전공명
    ex. 외과, 내과 등
     */
    @Column(nullable = false, unique = true)
    private String name;

    protected DepartmentEntity() {}

    public DepartmentEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department toDomainModel(){
        return new Department(this.id, this.name);
    }
}
