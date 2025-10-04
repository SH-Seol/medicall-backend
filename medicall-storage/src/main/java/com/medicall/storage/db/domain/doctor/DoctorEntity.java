package com.medicall.storage.db.domain.doctor;

import com.medicall.domain.doctor.Doctor;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.department.DepartmentEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
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
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentEntity> appointments;

    private String oauthId;

    private String oauthProvider;

    protected DoctorEntity() {}

    public DoctorEntity(String name, String imageUrl, String introduction, DepartmentEntity department
    , String oauthId, String oauthProvider) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.introduction = introduction;
        this.department = department;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
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

    public String getOauthId() {
        return oauthId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public Doctor toDomainModel(){
        return new Doctor(
                this.id,
                this.name,
                this.hospital != null ? this.hospital.toDomainModel() : null,
                this.introduction,
                this.imageUrl,
                this.department.toDomainModel(),
                this.oauthId,
                this.oauthProvider
        );
    }

    public void registerHospital(HospitalEntity hospital){
        this.hospital = hospital;
    }
}
