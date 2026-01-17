package com.medicall.storage.db.domain.patient;

import com.medicall.storage.db.domain.common.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chronic_diseases")
public class ChronicDiseaseEntity extends BaseEntity {

    private String name;

    protected ChronicDiseaseEntity(){}

    public ChronicDiseaseEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
