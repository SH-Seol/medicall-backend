package com.medicall.storage.db.core.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
}
