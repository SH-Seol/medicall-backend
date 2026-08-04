package com.medicall.storage.db.domain.invitation;

import java.time.LocalDateTime;

import com.medicall.domain.invitation.DoctorInvitation;
import com.medicall.domain.invitation.InvitationStatus;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 병원이 의사에게 발급하는 초대. 코드를 링크로 전달해 의사가 수락한다.
 */
@Entity
@Table(name = "doctor_invitations")
public class DoctorInvitationEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 32)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_doctor_id")
    private DoctorEntity acceptedDoctor;

    private LocalDateTime acceptedAt;

    protected DoctorInvitationEntity() {}

    public DoctorInvitationEntity(String code, HospitalEntity hospital, LocalDateTime expiresAt) {
        this.code = code;
        this.hospital = hospital;
        this.expiresAt = expiresAt;
        this.status = InvitationStatus.PENDING;
    }

    public String getCode() {
        return code;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public DoctorEntity getAcceptedDoctor() {
        return acceptedDoctor;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void accept(DoctorEntity doctor) {
        this.status = InvitationStatus.ACCEPTED;
        this.acceptedDoctor = doctor;
        this.acceptedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = InvitationStatus.CANCELED;
    }

    public DoctorInvitation toDomainModel() {
        return new DoctorInvitation(
                this.id,
                this.code,
                this.hospital.getId(),
                this.hospital.getName(),
                this.status,
                this.expiresAt,
                this.acceptedDoctor != null ? this.acceptedDoctor.getId() : null
        );
    }
}
