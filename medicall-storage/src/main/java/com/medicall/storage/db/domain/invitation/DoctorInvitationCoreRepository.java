package com.medicall.storage.db.domain.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.invitation.DoctorInvitation;
import com.medicall.domain.invitation.DoctorInvitationRepository;
import com.medicall.domain.invitation.NewDoctorInvitation;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.doctor.DoctorJpaRepository;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.hospital.HospitalJpaRepository;

@Repository
public class DoctorInvitationCoreRepository implements DoctorInvitationRepository {

    private final DoctorInvitationJpaRepository invitationJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;
    private final DoctorJpaRepository doctorJpaRepository;

    public DoctorInvitationCoreRepository(DoctorInvitationJpaRepository invitationJpaRepository,
                                          HospitalJpaRepository hospitalJpaRepository,
                                          DoctorJpaRepository doctorJpaRepository) {
        this.invitationJpaRepository = invitationJpaRepository;
        this.hospitalJpaRepository = hospitalJpaRepository;
        this.doctorJpaRepository = doctorJpaRepository;
    }

    public DoctorInvitation create(NewDoctorInvitation newInvitation) {
        HospitalEntity hospital = hospitalJpaRepository.findById(newInvitation.hospitalId())
                .orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));

        return invitationJpaRepository.save(new DoctorInvitationEntity(
                newInvitation.code(),
                hospital,
                newInvitation.expiresAt()
        )).toDomainModel();
    }

    public Optional<DoctorInvitation> findByCode(String code) {
        return invitationJpaRepository.findByCodeWithDetails(code).map(DoctorInvitationEntity::toDomainModel);
    }

    public List<DoctorInvitation> findAllByHospitalId(Long hospitalId) {
        return invitationJpaRepository.findAllByHospitalIdWithDetails(hospitalId).stream()
                .map(DoctorInvitationEntity::toDomainModel)
                .toList();
    }

    public void accept(Long invitationId, Long doctorId) {
        DoctorInvitationEntity invitation = invitationJpaRepository.findById(invitationId)
                .orElseThrow(() -> new CoreException(CoreErrorType.INVITATION_NOT_FOUND));
        DoctorEntity doctor = doctorJpaRepository.findById(doctorId)
                .orElseThrow(() -> new CoreException(CoreErrorType.DOCTOR_NOT_FOUND));

        invitation.accept(doctor);
    }

    public void cancel(Long invitationId) {
        DoctorInvitationEntity invitation = invitationJpaRepository.findById(invitationId)
                .orElseThrow(() -> new CoreException(CoreErrorType.INVITATION_NOT_FOUND));

        invitation.cancel();
    }
}
