-- =====================================================
-- Medicall Full Schema + Dataset
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- BASE TABLES
-- =====================================================

CREATE TABLE IF NOT EXISTS patients (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         age INT,
                         gender VARCHAR(20),
                         blood_type VARCHAR(10),
                         height DOUBLE,
                         weight DOUBLE,
                         date_of_birth DATE,
                         image_url VARCHAR(255),
                         email VARCHAR(255),
                         oauth_id VARCHAR(255),
                         oauth_provider VARCHAR(50),
                         emergency_contact_name VARCHAR(255),
                         emergency_contact_relationship VARCHAR(100),
                         emergency_contact_phone_number VARCHAR(50),
                         guardian_name VARCHAR(255),
                         guardian_relationship VARCHAR(100),
                         guardian_phone_number VARCHAR(50),
                         created_at DATETIME,
                         updated_at DATETIME,
                         CONSTRAINT uk_patient_oauth UNIQUE (oauth_provider, oauth_id),
                         CONSTRAINT uk_patient_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS hospitals (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          telephone_number VARCHAR(50),
                          image_url VARCHAR(255),
                          registration_status VARCHAR(50) NOT NULL,
                          oauth_id VARCHAR(255),
                          oauth_provider VARCHAR(50),
                          email VARCHAR(255),
                          created_at DATETIME,
                          updated_at DATETIME,
                          CONSTRAINT uk_hospital_oauth UNIQUE (oauth_provider, oauth_id),
                          CONSTRAINT uk_hospital_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS departments (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            created_at DATETIME,
                            updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS doctors (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        image_url VARCHAR(255),
                        introduction TEXT,
                        hospital_id BIGINT,
                        department_id BIGINT,
                        oauth_id VARCHAR(255),
                        oauth_provider VARCHAR(50),
                        created_at DATETIME,
                        updated_at DATETIME,
                        FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
                        FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS addresses (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         patient_id BIGINT,
                         zone_code VARCHAR(5) NOT NULL,
                         road_address VARCHAR(255) NOT NULL,
                         jibun_address VARCHAR(255),
                         detail_address VARCHAR(255),
                         building_name VARCHAR(255),
                         longitude DOUBLE NOT NULL,
                         latitude DOUBLE NOT NULL,
                         is_default BOOLEAN,
                         created_at DATETIME,
                         updated_at DATETIME,
                         FOREIGN KEY (patient_id) REFERENCES patients(id)
);

-- =====================================================
-- MAPPING TABLES
-- =====================================================

CREATE TABLE IF NOT EXISTS hospital_departments (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     hospital_id BIGINT NOT NULL,
                                     department_id BIGINT NOT NULL,
                                     created_at DATETIME,
                                     updated_at DATETIME,
                                     UNIQUE (hospital_id, department_id),
                                     FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
                                     FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE IF NOT EXISTS chronic_diseases (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255),
                                 created_at DATETIME,
                                 updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS patient_chronic_diseases (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         patient_id BIGINT NOT NULL,
                                         disease_id BIGINT NOT NULL,
                                         created_at DATETIME,
                                         updated_at DATETIME,
                                         FOREIGN KEY (patient_id) REFERENCES patients(id),
                                         FOREIGN KEY (disease_id) REFERENCES chronic_diseases(id)
);

-- =====================================================
-- APPOINTMENT
-- =====================================================

CREATE TABLE IF NOT EXISTS appointments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             patient_id BIGINT NOT NULL,
                             doctor_id BIGINT,
                             hospital_id BIGINT NOT NULL,
                             address_id BIGINT NOT NULL,
                             symptom TEXT NOT NULL,
                             reservation_time DATETIME NOT NULL,
                             status VARCHAR(50) NOT NULL,
                             created_at DATETIME,
                             updated_at DATETIME,
                             FOREIGN KEY (patient_id) REFERENCES patients(id),
                             FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                             FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
                             FOREIGN KEY (address_id) REFERENCES addresses(id)
);

-- =====================================================
-- TREATMENT
-- =====================================================

CREATE TABLE IF NOT EXISTS treatments (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           patient_id BIGINT NOT NULL,
                           doctor_id BIGINT NOT NULL,
                           hospital_id BIGINT NOT NULL,
                           symptom TEXT NOT NULL,
                           treatment TEXT NOT NULL,
                           detail_treatment TEXT,
                           prescription_id BIGINT UNIQUE,
                           created_at DATETIME,
                           updated_at DATETIME,
                           FOREIGN KEY (patient_id) REFERENCES patients(id),
                           FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                           FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

-- =====================================================
-- MEDICINE & PRESCRIPTION
-- =====================================================

CREATE TABLE IF NOT EXISTS medicines (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          manufacturer VARCHAR(255) NOT NULL,
                          unit VARCHAR(50),
                          created_at DATETIME,
                          updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS prescriptions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              patient_id BIGINT NOT NULL,
                              doctor_id BIGINT NOT NULL,
                              hospital_id BIGINT NOT NULL,
                              treatment_id BIGINT UNIQUE,
                              prescription_date DATE NOT NULL,
                              created_at DATETIME,
                              updated_at DATETIME,
                              FOREIGN KEY (patient_id) REFERENCES patients(id),
                              FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                              FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

CREATE TABLE IF NOT EXISTS prescription_medicines (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       prescription_id BIGINT NOT NULL,
                                       medicine_id BIGINT NOT NULL,
                                       dosage DOUBLE NOT NULL,
                                       dosage_unit VARCHAR(50),
                                       quantity INT NOT NULL,
                                       frequency VARCHAR(255) NOT NULL,
                                       instruction VARCHAR(255) NOT NULL,
                                       created_at DATETIME,
                                       updated_at DATETIME,
                                       FOREIGN KEY (prescription_id) REFERENCES prescriptions(id),
                                       FOREIGN KEY (medicine_id) REFERENCES medicines(id)
);
-- =====================================================
-- 5. DATA INSERT
-- =====================================================

-- HOSPITAL (3)
INSERT INTO hospitals (name, telephone_number, registration_status, email, oauth_id, oauth_provider, created_at)
VALUES
    ('메디콜 서울병원', '02-1111-1111', 'APPROVED', 'seoul@medicall.com', 'h1', 'kakao', NOW()),
    ('메디콜 부산병원', '051-2222-2222', 'APPROVED', 'busan@medicall.com', 'h2', 'kakao', NOW()),
    ('메디콜 대전병원', '042-3333-3333', 'APPROVED', 'daejeon@medicall.com', 'h3', 'kakao', NOW());

-- DEPARTMENT
INSERT INTO departments (name, created_at) VALUES
                                              ('내과', NOW()),
                                              ('외과', NOW()),
                                              ('정형외과', NOW()),
                                              ('피부과', NOW()),
                                              ('소아과', NOW());

-- DOCTOR (5)
INSERT INTO doctors (name, hospital_id, department_id, oauth_id, oauth_provider, created_at)
VALUES
    ('김의사', 1, 1, 'd1', 'google', NOW()),
    ('이의사', 1, 2, 'd2', 'google', NOW()),
    ('박의사', 2, 3, 'd3', 'google', NOW()),
    ('최의사', 2, 4, 'd4', 'google', NOW()),
    ('정의사', 3, 5, 'd5', 'google', NOW());

-- PATIENT (12)
INSERT INTO patients (name, age, gender, email, oauth_id, oauth_provider, created_at)
VALUES
    ('환자1', 25, 'MALE', 'p1@test.com', 'p1', 'kakao', NOW()),
    ('환자2', 30, 'FEMALE', 'p2@test.com', 'p2', 'kakao', NOW()),
    ('환자3', 40, 'MALE', 'p3@test.com', 'p3', 'kakao', NOW()),
    ('환자4', 35, 'FEMALE', 'p4@test.com', 'p4', 'kakao', NOW()),
    ('환자5', 50, 'MALE', 'p5@test.com', 'p5', 'kakao', NOW()),
    ('환자6', 28, 'FEMALE', 'p6@test.com', 'p6', 'kakao', NOW()),
    ('환자7', 33, 'MALE', 'p7@test.com', 'p7', 'kakao', NOW()),
    ('환자8', 45, 'FEMALE', 'p8@test.com', 'p8', 'kakao', NOW()),
    ('환자9', 60, 'MALE', 'p9@test.com', 'p9', 'kakao', NOW()),
    ('환자10', 22, 'FEMALE', 'p10@test.com', 'p10', 'kakao', NOW()),
    ('환자11', 38, 'MALE', 'p11@test.com', 'p11', 'kakao', NOW()),
    ('환자12', 41, 'FEMALE', 'p12@test.com', 'p12', 'kakao', NOW());

INSERT INTO addresses
(patient_id, zone_code, road_address, jibun_address,
 longitude, latitude, is_default, created_at, updated_at)
VALUES
    (1, '12345', '서울로', '지번', 127.0, 37.5, true, NOW(), NOW()),
    (2, '54321', '부산로', '지번', 129.0, 35.1, true, NOW(), NOW());

INSERT INTO appointments
(patient_id, doctor_id, hospital_id, address_id,
 symptom, reservation_time, status, created_at, updated_at)
VALUES
    (1, 1, 1, 1, '두통', NOW(), 'REQUESTED', NOW(), NOW()),
    (2, 2, 2, 2, '복통', NOW(), 'CONFIRMED', NOW(), NOW());

INSERT INTO medicines
(name, manufacturer, unit, created_at, updated_at)
VALUES
    ('타이레놀', '제약사A', 'mg', NOW(), NOW()),
    ('아스피린', '제약사B', 'mg', NOW(), NOW());

INSERT INTO treatments
(patient_id, doctor_id, hospital_id,
 symptom, treatment, detail_treatment, created_at, updated_at)
VALUES
    (1, 1, 1, '두통', '약물치료', '안정', NOW(), NOW()),
    (2, 2, 2, '복통', '검사', '초음파', NOW(), NOW());

INSERT INTO prescriptions
(patient_id, doctor_id, hospital_id, treatment_id,
 prescription_date, created_at, updated_at)
VALUES
    (1, 1, 1, 1, CURDATE(), NOW(), NOW()),
    (2, 2, 2, 2, CURDATE(), NOW(), NOW());

INSERT INTO prescription_medicines
(prescription_id, medicine_id, dosage, dosage_unit,
 quantity, frequency, instruction, created_at, updated_at)
VALUES
    (1, 1, 500, 'mg', 2, '1일 2회', '식후', NOW(), NOW()),
    (2, 2, 300, 'mg', 1, '1일 1회', '식전', NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;