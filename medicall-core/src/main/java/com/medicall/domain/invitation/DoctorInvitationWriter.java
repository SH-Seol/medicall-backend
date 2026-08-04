package com.medicall.domain.invitation;

import org.springframework.stereotype.Component;

@Component
public class DoctorInvitationWriter {

    private final DoctorInvitationRepository doctorInvitationRepository;

    public DoctorInvitationWriter(DoctorInvitationRepository doctorInvitationRepository) {
        this.doctorInvitationRepository = doctorInvitationRepository;
    }

    public DoctorInvitation create(NewDoctorInvitation newInvitation) {
        return doctorInvitationRepository.create(newInvitation);
    }

    public void accept(Long invitationId, Long doctorId) {
        doctorInvitationRepository.accept(invitationId, doctorId);
    }

    public void cancel(Long invitationId) {
        doctorInvitationRepository.cancel(invitationId);
    }
}
