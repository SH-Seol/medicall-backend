package com.medicall.storage.db.domain.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.medicall.domain.common.enums.AppointmentStatus;
import com.medicall.storage.db.domain.doctor.DoctorEntity;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findTop16ByPatientIdAndIdLessThanOrderByIdDesc(Long patientId, Long cursorId);
    List<AppointmentEntity> findTop16ByPatientIdOrderByIdDesc(Long patientId);
    /*
     * 중복 예약 검사는 아직 진행 중인 예약만 대상으로 한다.
     * 취소·거절·완료된 예약은 슬롯을 점유하지 않는다.(유니크 제약과 동일한 기준)
     */
    @Query("""
            SELECT COUNT(a) > 0 FROM AppointmentEntity a
            WHERE a.doctor.id = :doctorId
              AND a.reservationTime = :reservationTime
              AND a.status IN (com.medicall.domain.common.enums.AppointmentStatus.REQUESTED,
                               com.medicall.domain.common.enums.AppointmentStatus.ASSIGNED,
                               com.medicall.domain.common.enums.AppointmentStatus.EN_ROUTE,
                               com.medicall.domain.common.enums.AppointmentStatus.ARRIVED,
                               com.medicall.domain.common.enums.AppointmentStatus.IN_PROGRESS)
            """)
    boolean existsActiveByDoctorIdAndReservationTime(@Param("doctorId") Long doctorId,
                                                     @Param("reservationTime") LocalDateTime reservationTime);

    @Query("""
            SELECT COUNT(a) > 0 FROM AppointmentEntity a
            WHERE a.patient.id = :patientId
              AND a.reservationTime = :reservationTime
              AND a.status IN (com.medicall.domain.common.enums.AppointmentStatus.REQUESTED,
                               com.medicall.domain.common.enums.AppointmentStatus.ASSIGNED,
                               com.medicall.domain.common.enums.AppointmentStatus.EN_ROUTE,
                               com.medicall.domain.common.enums.AppointmentStatus.ARRIVED,
                               com.medicall.domain.common.enums.AppointmentStatus.IN_PROGRESS)
            """)
    boolean existsActiveByPatientIdAndReservationTime(@Param("patientId") Long patientId,
                                                      @Param("reservationTime") LocalDateTime reservationTime);

    /*
     * 아래 상태 전이는 모두 조건부 UPDATE로 수행한다.
     * 환자·병원 앱이 같은 예약을 동시에 조작할 수 있어, 현재 상태를 읽고 판단한 뒤 쓰면
     * 서로의 변경을 덮어쓴다. WHERE 절에 기대하는 상태를 넣어 DB가 판단과 갱신을 함께 처리한다.
     * 반환값이 0이면 그 사이 상태가 바뀐 것이다.
     */

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE AppointmentEntity a
            SET a.status = com.medicall.domain.common.enums.AppointmentStatus.ASSIGNED
            WHERE a.id = :appointmentId
              AND a.hospital.id = :hospitalId
              AND a.status = com.medicall.domain.common.enums.AppointmentStatus.REQUESTED
              AND a.doctor IS NOT NULL
            """)
    int acceptIfRequested(@Param("appointmentId") Long appointmentId, @Param("hospitalId") Long hospitalId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE AppointmentEntity a
            SET a.status = com.medicall.domain.common.enums.AppointmentStatus.REJECTED
            WHERE a.id = :appointmentId
              AND a.hospital.id = :hospitalId
              AND a.status = com.medicall.domain.common.enums.AppointmentStatus.REQUESTED
            """)
    int rejectIfRequested(@Param("appointmentId") Long appointmentId, @Param("hospitalId") Long hospitalId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE AppointmentEntity a
            SET a.status = com.medicall.domain.common.enums.AppointmentStatus.CANCELLED
            WHERE a.id = :appointmentId
              AND a.patient.id = :patientId
              AND a.status IN (com.medicall.domain.common.enums.AppointmentStatus.REQUESTED,
                               com.medicall.domain.common.enums.AppointmentStatus.ASSIGNED)
            """)
    int cancelIfCancelable(@Param("appointmentId") Long appointmentId, @Param("patientId") Long patientId);

    /**
     * 의사가 배정되지 않은 예약에만 의사를 지정한다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE AppointmentEntity a
            SET a.doctor = :doctor
            WHERE a.id = :appointmentId
              AND a.hospital.id = :hospitalId
              AND a.status = com.medicall.domain.common.enums.AppointmentStatus.REQUESTED
              AND a.doctor IS NULL
            """)
    int assignDoctorIfUnassigned(@Param("appointmentId") Long appointmentId,
                                 @Param("hospitalId") Long hospitalId,
                                 @Param("doctor") DoctorEntity doctor);

    /**
     * 방문 진행 전이. 의사 본인의 예약이고 기대하는 현재 상태일 때만 바뀐다.
     * "다음 환자"를 연달아 눌러도 en_route_doctor_key 유니크 제약이 두 번째 이동을 막는다.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE AppointmentEntity a
            SET a.status = :nextStatus
            WHERE a.id = :appointmentId
              AND a.doctor.id = :doctorId
              AND a.status = :expectedStatus
            """)
    int updateStatusByDoctor(@Param("appointmentId") Long appointmentId,
                             @Param("doctorId") Long doctorId,
                             @Param("expectedStatus") AppointmentStatus expectedStatus,
                             @Param("nextStatus") AppointmentStatus nextStatus);
}
