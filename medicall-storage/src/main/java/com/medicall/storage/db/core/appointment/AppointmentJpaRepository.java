package com.medicall.storage.db.core.appointment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findTop16ByPatientIdAndIdLessThanOrderByIdDesc(Long patientId, Long cursorId);
    List<AppointmentEntity> findTop16ByPatientIdOrderByIdDesc(Long patientId);
}
