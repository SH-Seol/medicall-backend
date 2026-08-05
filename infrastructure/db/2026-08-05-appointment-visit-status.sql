-- 방문 진행 상태 추가 (출발 → 도착 → 진료 중)
-- status는 문자열 컬럼이라 값 추가에 스키마 변경이 필요 없지만,
-- 슬롯 점유 판정에 새 상태를 포함해야 하므로 생성 컬럼을 다시 만든다.

-- status는 MySQL ENUM 컬럼이라 새 값을 먼저 추가해야 한다.
-- (Hibernate가 알파벳 순으로 생성하므로 같은 순서를 유지한다)
ALTER TABLE appointments
  MODIFY COLUMN status ENUM('ARRIVED','ASSIGNED','CANCELLED','COMPLETED','EN_ROUTE','IN_PROGRESS','REJECTED','REQUESTED') NOT NULL;

ALTER TABLE appointments
  DROP INDEX uk_appointment_active_slot,
  DROP COLUMN active_slot_key,
  DROP INDEX uk_appointment_patient_slot,
  DROP COLUMN patient_slot_key;

ALTER TABLE appointments
  ADD COLUMN active_slot_key VARCHAR(64)
    GENERATED ALWAYS AS (
      CASE
        WHEN status IN ('REQUESTED', 'ASSIGNED', 'EN_ROUTE', 'ARRIVED', 'IN_PROGRESS')
             AND doctor_id IS NOT NULL
        THEN CONCAT(doctor_id, '@', reservation_time)
        ELSE NULL
      END
    ) STORED,
  ADD UNIQUE KEY uk_appointment_active_slot (active_slot_key),
  ADD COLUMN patient_slot_key VARCHAR(64)
    GENERATED ALWAYS AS (
      CASE
        WHEN status IN ('REQUESTED', 'ASSIGNED', 'EN_ROUTE', 'ARRIVED', 'IN_PROGRESS')
        THEN CONCAT(patient_id, '@', reservation_time)
        ELSE NULL
      END
    ) STORED,
  ADD UNIQUE KEY uk_appointment_patient_slot (patient_slot_key);

-- 의사는 동시에 한 곳으로만 이동할 수 있다.
-- "다음 환자"를 연달아 누르거나 두 기기에서 동시에 눌러도 하나만 성공하도록 막는다.
ALTER TABLE appointments
  ADD COLUMN en_route_doctor_key VARCHAR(32)
    GENERATED ALWAYS AS (
      CASE
        WHEN status IN ('EN_ROUTE', 'ARRIVED', 'IN_PROGRESS') AND doctor_id IS NOT NULL
        THEN CONCAT('doctor:', doctor_id)
        ELSE NULL
      END
    ) STORED,
  ADD UNIQUE KEY uk_appointment_en_route_doctor (en_route_doctor_key);
