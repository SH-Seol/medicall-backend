-- 처방전 조제 상태
-- 약국이 QR을 스캔해 조제를 완료하면 상태가 바뀐다. 조제는 한 번만 가능하다.
ALTER TABLE prescriptions
  ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ISSUED',
  ADD COLUMN dispensed_at DATETIME(6) NULL;
