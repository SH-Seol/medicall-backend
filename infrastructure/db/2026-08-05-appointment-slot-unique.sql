-- 이중 예약 방지
-- 같은 의사·같은 시간대에 유효한 예약이 둘 이상 생기지 않도록 유니크 제약을 건다.
-- MySQL은 부분 인덱스를 지원하지 않으므로, 유효하지 않은 예약은 NULL이 되는 생성 컬럼을 사용한다.
-- (유니크 인덱스에서 NULL끼리는 충돌하지 않는다)
ALTER TABLE appointments
  ADD COLUMN active_slot_key VARCHAR(64)
    GENERATED ALWAYS AS (
      CASE
        WHEN status IN ('REQUESTED', 'ASSIGNED') AND doctor_id IS NOT NULL
        THEN CONCAT(doctor_id, '@', reservation_time)
        ELSE NULL
      END
    ) STORED,
  ADD UNIQUE KEY uk_appointment_active_slot (active_slot_key);

-- 같은 환자가 같은 시간대에 중복 예약하는 것도 막는다.
ALTER TABLE appointments
  ADD COLUMN patient_slot_key VARCHAR(64)
    GENERATED ALWAYS AS (
      CASE
        WHEN status IN ('REQUESTED', 'ASSIGNED')
        THEN CONCAT(patient_id, '@', reservation_time)
        ELSE NULL
      END
    ) STORED,
  ADD UNIQUE KEY uk_appointment_patient_slot (patient_slot_key);
