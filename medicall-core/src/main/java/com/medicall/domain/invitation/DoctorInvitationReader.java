package com.medicall.domain.invitation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

@Component
public class DoctorInvitationReader {

    private final DoctorInvitationRepository doctorInvitationRepository;

    public DoctorInvitationReader(DoctorInvitationRepository doctorInvitationRepository) {
        this.doctorInvitationRepository = doctorInvitationRepository;
    }

    public DoctorInvitation findByCode(String code) {
        return doctorInvitationRepository.findByCode(code)
                .orElseThrow(() -> new CoreException(CoreErrorType.INVITATION_NOT_FOUND));
    }

    public List<DoctorInvitation> findAllByHospitalId(Long hospitalId) {
        return doctorInvitationRepository.findAllByHospitalId(hospitalId);
    }
}
