package com.medicall.storage.db.domain.prescription;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrescriptionJpaRepository extends JpaRepository<PrescriptionEntity, Long> {

    /**
     * 아직 조제되지 않은 처방전만 조제 완료로 바꾼다.
     * 약국 두 곳에서 동시에 처리해도 한 번만 성공한다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE PrescriptionEntity p
            SET p.status = com.medicall.domain.common.enums.PrescriptionStatus.DISPENSED,
                p.dispensedAt = :dispensedAt
            WHERE p.id = :prescriptionId
              AND p.status = com.medicall.domain.common.enums.PrescriptionStatus.ISSUED
            """)
    int dispenseIfIssued(@Param("prescriptionId") Long prescriptionId,
                         @Param("dispensedAt") LocalDateTime dispensedAt);
}
