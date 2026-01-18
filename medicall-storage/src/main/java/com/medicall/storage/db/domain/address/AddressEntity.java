package com.medicall.storage.db.domain.address;

import com.medicall.domain.address.Address;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.patient.PatientEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "addresses")
public class AddressEntity extends BaseEntity {

    // 카카오 API의 우편번호 (5자리)
    @Column(nullable = false, length = 5)
    private String zoneCode;

    // 도로명 주소
    @Column(nullable = false, length = 255)
    private String roadAddress;

    // 지번 주소
    @Column(length = 255)
    private String jibunAddress;

    // 상세 주소
    @Column(length = 255)
    private String detailAddress;

    // 참고항목 ex. (삼성동, 삼성아파트) 또는 특정 건물명
    @Column(length = 255)
    private String buildingName;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    private boolean isDefault;

    protected AddressEntity() {}

    public AddressEntity(String zoneCode, String roadAddress, String jibunAddress,
                   String detailAddress, String buildingName, Double longitude, Double latitude) {
        this.zoneCode = Objects.requireNonNull(zoneCode, "우편번호는 필수입니다.");
        this.roadAddress = Objects.requireNonNull(roadAddress, "도로명 주소는 필수입니다.");
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
        this.buildingName = buildingName;
        this.longitude = Objects.requireNonNull(longitude, "경도(longitude)는 필수입니다.");
        this.latitude = Objects.requireNonNull(latitude, "위도(latitude)는 필수입니다.");
        this.isDefault = false;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public Address toDomainModel(){
        return new Address(
                this.zoneCode,
                this.roadAddress,
                this.jibunAddress,
                this.detailAddress,
                this.buildingName,
                this.longitude,
                this.latitude,
                this.isDefault
        );
    }
}
