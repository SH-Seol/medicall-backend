# Medicall - 방문 의료 서비스 <img src="./docs/images/icon.png" align="left" width="50">

---

## 프로젝트 개요

의사와 환자, 병원을 상호 연결하는 의료 통합 플랫폼입니다.

#### 정부의 "일차 의료 방문 진료 수가 시범 사업"에 영감을 받아 제작하게 된 프로젝트입니다.

### 주요 기능

- 의사: 예약 관리, 진단 기록 및 처방전 작성
- 환자: 저장된 주소 기반 병원 검색 및 예약, 처방전 QR 코드로 출력
- 병원: 의료진 관리, 예약 관리, 진료 기록 관리

---

## 기술 스택
### Backend

- Java 17
- Gradle 8.14
- Spring Boot 3.5.5
- Mysql 8.x
- Redis 7.x
- Spring Data JPA
- QueryDSL

### Infra

- Docker

---

## 모듈 구조
![](docs/images/medicall-arch-dg.png)

#### 모노레포 멀티모듈 구조

- api(doctor, patient, hospital, common)
- core
- storage

### 모듈 설명
- medicall-api-common

  - 3개의 api에서 공통으로 사용하는 모듈입니다.
  - 보안, 현재 사용자 관련 정보를 담고 있습니다.
  - medicall-core 모듈을 의존합니다.

- medicall-api-patient

  - 환자 전용 앱 서비스에서 사용하는 모듈입니다.
  - SpringBoot 앱을 실행하며, 환자의 요청을 처리합니다.
  - medicall-api-common 과 medicall-core 모듈을 의존합니다.

- medicall-api-doctor

  - 의사 전용 앱 서비스에서 사용하는 모듈입니다.
  - SpringBoot 앱을 실행하며, 의사의 요청을 처리합니다.
  - medicall-api-common 과 medicall-core 모듈을 의존합니다.

- medicall-api-hospital

  - 병원 전용 앱 서비스에서 사용하는 모듈입니다.
  - SpringBoot 앱을 실행하며, 병원의 요청을 처리합니다.
  - medicall-api-common 과 medicall-core 모듈을 의존합니다.

- medicall-core

  - 비즈니스 로직, 도메인 모델(record 타입)을 담고 있는 모듈입니다.
  - service, reader/writer, repository(interface)가 정의됩니다.
  - reader/writer에서 에러를 처리합니다.
  - repository(interface)에서 storage 모듈과 상호작용합니다.

- medicall-storage

  - JPA Entity와 coreRepository가 정의됩니다.
  - DB와 상호작용을 합니다.

---

## 데이터베이스 스키마

### 주요 테이블

- doctors: 의사 정보
- hospitals: 병원 정보
- patients: 환자 정보
- appointments: 예약 내역
- prescriptions: 처방 내역
- treatments: 진단 기록

### 특징

- 커서 기반 페이징

---

## 🔐 보안
### 인증/인가

- JWT 토큰 기반 인증
- 사용자별 권한 분리 (doctor/patient/hospital)
- API 엔드포인트별 접근 제어
---

## 📝 코딩 컨벤션

### Java Style Guide
- **우아한테크코스 Java 스타일 가이드** 준수
- 4칸 스페이스 들여쓰기
- 120자 라인 길이 제한
- 모든 제어문에 중괄호 필수
- Import 순서: static → java → jakarta → org → com

### IntelliJ IDEA 설정
프로젝트 루트의 `/docs/styleguide/intellij-medicall-java-styleguide.xml` 파일을 IDE에 적용해주세요.

---


### Reference
<a href="https://www.flaticon.com/kr/free-icons/" title="병원 아이콘">병원 아이콘 제작자: meaicon - Flaticon</a>