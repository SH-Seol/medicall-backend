-- 진료과 / 세부전공 기초 데이터
-- 기존 major 테이블(구 스키마)의 진료과를 departments로 이관하고 세부전공을 추가한다.

INSERT IGNORE INTO departments (name, created_at)
SELECT title, NOW() FROM major;

INSERT IGNORE INTO departments (name, created_at) VALUES
  ('내과', NOW()), ('외과', NOW()), ('소아청소년과', NOW()), ('산부인과', NOW()),
  ('정신건강의학과', NOW()), ('안과', NOW()), ('치과', NOW()), ('비뇨의학과', NOW());

INSERT IGNORE INTO specialties (name, department_id, created_at)
SELECT s.name, d.id, NOW()
FROM departments d
JOIN (
  SELECT '내과' AS dept, '심장내과' AS name UNION ALL
  SELECT '내과', '소화기내과' UNION ALL
  SELECT '내과', '호흡기내과' UNION ALL
  SELECT '내과', '내분비내과' UNION ALL
  SELECT '내과', '신장내과' UNION ALL
  SELECT '내과', '감염내과' UNION ALL
  SELECT '외과', '대장항문외과' UNION ALL
  SELECT '외과', '간담췌외과' UNION ALL
  SELECT '외과', '유방갑상선외과' UNION ALL
  SELECT '정형외과', '척추' UNION ALL
  SELECT '정형외과', '관절' UNION ALL
  SELECT '정형외과', '수족부' UNION ALL
  SELECT '정형외과', '스포츠의학' UNION ALL
  SELECT '소아청소년과', '소아신경' UNION ALL
  SELECT '소아청소년과', '소아소화기' UNION ALL
  SELECT '소아청소년과', '신생아' UNION ALL
  SELECT '이비인후과', '이과' UNION ALL
  SELECT '이비인후과', '비과' UNION ALL
  SELECT '이비인후과', '두경부' UNION ALL
  SELECT '피부과', '피부암' UNION ALL
  SELECT '피부과', '알레르기' UNION ALL
  SELECT '안과', '망막' UNION ALL
  SELECT '안과', '각막' UNION ALL
  SELECT '안과', '녹내장' UNION ALL
  SELECT '정신건강의학과', '소아청소년정신' UNION ALL
  SELECT '정신건강의학과', '중독정신' UNION ALL
  SELECT '재활의학과', '신경재활' UNION ALL
  SELECT '재활의학과', '근골격재활'
) s ON s.dept = d.name;
