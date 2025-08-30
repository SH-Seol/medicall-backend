package com.medicall.storage.db.core.doctor;

import com.medicall.domain.doctor.Doctor;
import com.medicall.storage.db.core.appointment.AppointmentEntity;
import com.medicall.storage.db.core.common.domain.BaseEntity;
import com.medicall.storage.db.core.department.DepartmentEntity;
import com.medicall.storage.db.core.hospital.HospitalEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class DoctorEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String imageUrl;

    private String introduction;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private DepartmentEntity department;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments;

    protected DoctorEntity() {}

    public DoctorEntity(String name, String imageUrl, String introduction, DepartmentEntity department) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public String getIntroduction() {
        return introduction;
    }

    public DepartmentEntity getDepartment() {
        return department;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public Doctor toDomainModel(){
        return new Doctor(
                this.id,
                this.name,
                this.hospital != null ? this.hospital.toDomainModel() : null,
                this.introduction,
                this.imageUrl,
                this.department.toDomainModel()
        );
    }

    public void registerHospital(HospitalEntity hospital){
        this.hospital = hospital;
    }
}
