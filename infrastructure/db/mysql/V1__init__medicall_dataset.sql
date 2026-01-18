-- =====================================================
-- Medicall FULL RESET + SCHEMA + DATASET
-- =====================================================

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- BASE TABLES
-- =====================================================

CREATE TABLE patients (
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

CREATE TABLE hospitals (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           telephone_number VARCHAR(50),
                           image_url VARCHAR(255),
                           registration_status VARCHAR(50) NOT NULL,
                           oauth_id VARCHAR(255),
                           oauth_provider VARCHAR(50),
                           email VARCHAR(255),
                           address_id BIGINT,
                           created_at DATETIME,
                           updated_at DATETIME,
                           CONSTRAINT uk_hospital_oauth UNIQUE (oauth_provider, oauth_id),
                           CONSTRAINT uk_hospital_email UNIQUE (email)
);

CREATE TABLE departments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL UNIQUE,
                             created_at DATETIME,
                             updated_at DATETIME
);

CREATE TABLE doctors (
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

CREATE TABLE addresses (
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

ALTER TABLE hospitals
    ADD CONSTRAINT fk_hospital_address
        FOREIGN KEY (address_id) REFERENCES addresses(id);

-- =====================================================
-- MAPPING TABLES
-- =====================================================

CREATE TABLE hospital_departments (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      hospital_id BIGINT NOT NULL,
                                      department_id BIGINT NOT NULL,
                                      created_at DATETIME,
                                      updated_at DATETIME,
                                      UNIQUE (hospital_id, department_id),
                                      FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
                                      FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE chronic_diseases (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  name VARCHAR(255),
                                  created_at DATETIME,
                                  updated_at DATETIME
);

CREATE TABLE patient_chronic_diseases (
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

CREATE TABLE appointments (
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

CREATE TABLE treatments (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            patient_id BIGINT NOT NULL,
                            doctor_id BIGINT NOT NULL,
                            hospital_id BIGINT NOT NULL,
                            symptom TEXT NOT NULL,
                            treatment TEXT NOT NULL,
                            detail_treatment TEXT,
                            created_at DATETIME,
                            updated_at DATETIME,
                            FOREIGN KEY (patient_id) REFERENCES patients(id),
                            FOREIGN KEY (doctor_id) REFERENCES doctors(id),
                            FOREIGN KEY (hospital_id) REFERENCES hospitals(id)
);

-- =====================================================
-- MEDICINE & PRESCRIPTION
-- =====================================================

CREATE TABLE medicines (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           manufacturer VARCHAR(255) NOT NULL,
                           unit VARCHAR(50),
                           created_at DATETIME,
                           updated_at DATETIME
);

CREATE TABLE prescriptions (
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
                               FOREIGN KEY (hospital_id) REFERENCES hospitals(id),
                               FOREIGN KEY (treatment_id) REFERENCES treatments(id)
);

CREATE TABLE prescription_medicines (
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
-- DATA INSERT
-- =====================================================

INSERT INTO hospitals (name, telephone_number, registration_status, email, oauth_id, oauth_provider, created_at)
VALUES
    ('메디콜 서울병원', '02-1111-1111', 'APPROVED', 'seoul@medicall.com', 'h1', 'kakao', NOW()),
    ('메디콜 부산병원', '051-2222-2222', 'APPROVED', 'busan@medicall.com', 'h2', 'kakao', NOW()),
    ('메디콜 대전병원', '042-3333-3333', 'APPROVED', 'daejeon@medicall.com', 'h3', 'kakao', NOW());

INSERT INTO departments (name, created_at) VALUES
                                               ('내과', NOW()), ('외과', NOW()), ('정형외과', NOW()), ('피부과', NOW()), ('소아과', NOW());

INSERT INTO doctors (name, hospital_id, department_id, oauth_id, oauth_provider, created_at)
VALUES
    ('김의사', 1, 1, 'd1', 'kakao', NOW()),
    ('이의사', 1, 2, 'd2', 'kakao', NOW()),
    ('박의사', 2, 3, 'd3', 'kakao', NOW()),
    ('최의사', 2, 4, 'd4', 'kakao', NOW()),
    ('정의사', 3, 5, 'd5', 'kakao', NOW());

INSERT INTO patients (name, age, gender, email, oauth_id, oauth_provider, created_at)
VALUES
    ('환자1', 25, 'MALE', 'p1@test.com', 'p1', 'kakao', NOW()),
    ('환자2', 30, 'FEMALE', 'p2@test.com', 'p2', 'kakao', NOW());

INSERT INTO addresses
(patient_id, zone_code, road_address, jibun_address, longitude, latitude, is_default, created_at, updated_at)
VALUES
    (1, '12345', '서울로', '지번', 127.0, 37.5, true, NOW(), NOW()),
    (2, '54321', '부산로', '지번', 129.0, 35.1, true, NOW(), NOW());

UPDATE hospitals SET address_id = 1 WHERE id = 1;
UPDATE hospitals SET address_id = 2 WHERE id = 2;

SET FOREIGN_KEY_CHECKS = 0;