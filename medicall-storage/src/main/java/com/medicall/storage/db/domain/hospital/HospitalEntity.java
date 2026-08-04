package com.medicall.storage.db.domain.hospital;

import com.medicall.domain.hospital.Hospital;
import com.medicall.storage.db.domain.address.AddressEntity;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.domain.common.enums.BusinessStatus;
import com.medicall.storage.db.domain.common.enums.RegistrationStatus;
import com.medicall.storage.db.domain.department.DepartmentEntity;
import com.medicall.storage.db.domain.department.HospitalDepartmentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "hospitals",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_hospital_oauth",
                        columnNames = {"oauth_provider", "oauth_id"}
                ),
                @UniqueConstraint(
                        name = "uk_hospital_email",
                        columnNames = {"email"}
                )
        }
)
public class HospitalEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    private String telephoneNumber;

    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HospitalDepartmentEntity> departments = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<AppointmentEntity> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OperatingTimeEntity> operatingTimes = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HolidayOperatingTimeEntity> holidaysOperatingTimes = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    @Transient
    @Enumerated(EnumType.STRING)
    private BusinessStatus businessStatus;

    private String oauthId;

    private String oauthProvider;

    private String email;

    protected HospitalEntity() {}

    public HospitalEntity(String name, String imageUrl, String oauthId, String oauthProvider, String email) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.registrationStatus = RegistrationStatus.PENDING;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public List<HospitalDepartmentEntity> getDepartments() {
        return departments;
    }

    public List<OperatingTimeEntity> getOperatingTimes() {
        return operatingTimes;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public List<HolidayOperatingTimeEntity> getHolidaysOperatingTimes() {
        return holidaysOperatingTimes;
    }

    public BusinessStatus getBusinessStatus() {
        return businessStatus;
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

    public String getEmail() {
        return email;
    }

    public void addTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void addDepartments(List<DepartmentEntity> departments) {
        departments.forEach(this::addDepartment);
    }

    /**
     * 진료과를 전달한 목록으로 전체 교체한다.
     */
    public void replaceDepartments(List<DepartmentEntity> departments) {
        this.departments.clear();
        addDepartments(departments);
    }

    public void addDepartment(DepartmentEntity department) {
        HospitalDepartmentEntity hospitalDepartmentEntity = new HospitalDepartmentEntity(this, department);
        departments.add(hospitalDepartmentEntity);
    }

    public void addOperatingTimes(List<OperatingTimeEntity> operatingTimes) {
        operatingTimes.forEach(this::addOperatingTime);
    }

    public void addOperatingTime(OperatingTimeEntity operatingTime) {
        operatingTimes.add(operatingTime);
    }

    public void addAddress(AddressEntity address) {
        this.address = address;
    }

    /**
     * 내 정보 수정 (null인 항목은 기존 값을 유지한다)
     */
    public void updateProfile(String name, String telephoneNumber, String imageUrl) {
        if (name != null) {
            this.name = name;
        }
        if (telephoneNumber != null) {
            this.telephoneNumber = telephoneNumber;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public Hospital toDomainModel(){
        return new Hospital(
                this.id,
                this.name,
                this.telephoneNumber,
                this.address != null ? this.address.toDomainModel() : null,
                this.imageUrl,
                this.departments.stream().map(HospitalDepartmentEntity::getDepartment).map(DepartmentEntity::toDomainModel).toList(),
                this.operatingTimes.stream().map(OperatingTimeEntity::toDomainModel).toList(),
                this.businessStatus
        );
    }
}
