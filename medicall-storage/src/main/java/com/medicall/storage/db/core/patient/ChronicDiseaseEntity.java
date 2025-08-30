package com.medicall.storage.db.core.patient;

import com.medicall.storage.db.core.common.domain.BaseEntity;
import jakarta.persistence.Entity;

@Entity
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
