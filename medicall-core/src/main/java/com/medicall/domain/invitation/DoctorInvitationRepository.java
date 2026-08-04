package com.medicall.domain.invitation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface DoctorInvitationRepository {
    DoctorInvitation create(NewDoctorInvitation newInvitation);
    Optional<DoctorInvitation> findByCode(String code);
    List<DoctorInvitation> findAllByHospitalId(Long hospitalId);
    void accept(Long invitationId, Long doctorId);
    void cancel(Long invitationId);
}
