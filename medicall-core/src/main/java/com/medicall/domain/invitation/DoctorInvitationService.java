package com.medicall.domain.invitation;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.doctor.Doctor;
import com.medicall.domain.doctor.DoctorReader;
import com.medicall.domain.doctor.DoctorWriter;
import com.medicall.domain.hospital.HospitalReader;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

/**
 * 병원이 의사를 초대하고, 의사가 초대 코드로 병원에 소속되는 흐름을 담당한다.
 */
@Service
public class DoctorInvitationService {

    private static final Duration DEFAULT_VALIDITY = Duration.ofDays(7);
    private static final String CODE_CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 8;

    private final DoctorInvitationReader invitationReader;
    private final DoctorInvitationWriter invitationWriter;
    private final HospitalReader hospitalReader;
    private final DoctorReader doctorReader;
    private final DoctorWriter doctorWriter;
    private final SecureRandom random = new SecureRandom();

    public DoctorInvitationService(DoctorInvitationReader invitationReader,
                                   DoctorInvitationWriter invitationWriter,
                                   HospitalReader hospitalReader,
                                   DoctorReader doctorReader,
                                   DoctorWriter doctorWriter) {
        this.invitationReader = invitationReader;
        this.invitationWriter = invitationWriter;
        this.hospitalReader = hospitalReader;
        this.doctorReader = doctorReader;
        this.doctorWriter = doctorWriter;
    }

    /**
     * 병원이 초대 코드를 발급한다.
     */
    @Transactional
    public DoctorInvitation invite(Long hospitalId) {
        hospitalReader.findById(hospitalId);

        return invitationWriter.create(new NewDoctorInvitation(
                hospitalId,
                generateCode(),
                LocalDateTime.now().plus(DEFAULT_VALIDITY)
        ));
    }

    @Transactional(readOnly = true)
    public List<DoctorInvitation> getInvitations(Long hospitalId) {
        return invitationReader.findAllByHospitalId(hospitalId);
    }

    /**
     * 의사가 수락 전에 어떤 병원의 초대인지 확인한다.
     */
    @Transactional(readOnly = true)
    public DoctorInvitation preview(String code) {
        DoctorInvitation invitation = invitationReader.findByCode(code);
        validateUsable(invitation);

        return invitation;
    }

    /**
     * 의사가 초대를 수락해 병원에 소속된다.
     */
    @Transactional
    public DoctorInvitation accept(String code, Long doctorId) {
        DoctorInvitation invitation = invitationReader.findByCode(code);
        validateUsable(invitation);

        Doctor doctor = doctorReader.findById(doctorId);
        if (doctor.hospital() != null) {
            throw new CoreException(CoreErrorType.DOCTOR_BELONGS_TO_HOSPITAL);
        }

        doctorWriter.registerHospital(doctorId, invitation.hospitalId());
        invitationWriter.accept(invitation.id(), doctorId);

        return invitationReader.findByCode(code);
    }

    @Transactional
    public void cancel(Long hospitalId, String code) {
        DoctorInvitation invitation = invitationReader.findByCode(code);
        if (!invitation.hospitalId().equals(hospitalId)) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_FOUND);
        }
        if (!invitation.isPending()) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_USABLE);
        }

        invitationWriter.cancel(invitation.id());
    }

    private void validateUsable(DoctorInvitation invitation) {
        if (!invitation.isPending()) {
            throw new CoreException(CoreErrorType.INVITATION_NOT_USABLE);
        }
        if (invitation.isExpired(LocalDateTime.now())) {
            throw new CoreException(CoreErrorType.INVITATION_EXPIRED);
        }
    }

    /**
     * 혼동하기 쉬운 문자(0/O, 1/I)를 제외한 8자리 코드
     */
    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CODE_CHARSET.charAt(random.nextInt(CODE_CHARSET.length())));
        }
        return code.toString();
    }
}
