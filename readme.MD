# 에이치디정션 백엔드 개발자 채용 온라인 과제
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white&style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white&style=flat-square)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white&style=flat-square)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?logo=intellijidea&logoColor=white&style=flat-square)

> **병원의 환자의 기본 정보 및 내원 이력을 관리할 수 있는 백엔드 시스템**
---
## 1) 프로젝트 개요
이 프로젝트는 과제 요구사항에 따라 **병원의 환자 정보를 통합적으로 관리할 수 있는 백엔드 시스템**을 구현한 것입니다.  
단순 CRUD에 그치지 않고, **환자등록번호의 병원별 유일성 보장**, **환자 상세 조회 시 내원 기록 연동**, **검색 및 페이징 처리** 등 실제 운영 환경에서 필요한 조건들을 반영했습니다.  

### 주요 기능
- 병원 정보 관리
- 환자 등록 (병원별 중복되지 않는 환자등록번호 자동 생성)
- 환자 수정 및 삭제
- 환자 상세 조회 (내원 기록 포함)
- 동적 검색 및 페이징 기능을 통한 환자 목록 조회
- 방문 이력 관리 (CRUD 작업)

## 2) 기술 스택

- 언어: Kotlin
- JDK: 21
- 프레임워크: Spring Boot 3.4.9
- 데이터베이스: H2 (In-memory, 개발/테스트용)  
- ORM: Spring Data JPA
- 동적 쿼리: QueryDSL
- 빌드: Gradle
- 테스트: JUnit5, Spring Test  

## 3) 실행 방법 (로컬)

### 사전 준비
- JDK 21 이상 (Amazon Corretto 권장)
- IntelliJ IDEA 또는 선호하는 IDE

### 실행 단계
1. 저장소 클론
   ```bash
   git clone https://github.com/kyhyeok/junction
   cd junction
   ```

2. 빌드 및 실행
   ```bash
   ./gradlew clean bootRun
   ```

3. 접속 정보
   - 서버 기본 포트: [http://localhost:8080](http://localhost:8080)  
   - H2 콘솔: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
     - JDBC URL: `jdbc:h2:mem:testdb`  
     - 사용자명: `sa`  
     - 비밀번호: (없음)

### 테스트 실행
```bash
./gradlew test
```
- JUnit5 기반 테스트가 실행되며, 성공 여부를 확인할 수 있습니다.

## 4) API 스펙

### 환자 등록
- URL: /api/v1/patients
- Method: POST
- 설명: 병원별 중복되지 않는 환자등록번호를 자동 생성하여 환자 정보를 등록합니다.
- 요청 예시:
```
{
  "hospitalId": 1,
  "patientName": "김환자",
  "genderCode": "F",
  "birthDay": "1990-05-31",
  "phoneNumber": "010-1111-2222"
}
```

### 환자 수정
- URL: /api/v1/patients/{id}
- Method: POST
- 설명: 기존 환자 정보를 수정합니다.
- 요청 예시:
```
{
  "hospitalId": 1,
  "patientName": "김환자 수정",
  "genderCode": "F",
  "birthDay": "1990-05-31",
  "phoneNumber": "010-1111-9999"
}
```

### 환자 삭제
- URL: /api/v1/patients/{id}
- Method: DELETE
- 설명: 환자 정보를 삭제합니다.

### 환자 상세 조회
- URL: /api/v1/patients/{id}
- Method: GET
- 설명: 환자 Entity의 모든 속성과 내원 정보를 포함한 상세 정보를 조회합니다.

### 환자 목록 조회 (동적 검색 및 페이징)
- URL: /api/v1/patients
- Method: GET
- 설명: 환자 이름, 환자등록번호, 생년월일에 따른 동적 검색 조건과 페이지 번호, 페이지 크기, 정렬 옵션을 이용하여 환자 목록을 조회합니다.
- 요청 파라미터
    - patientName (optional): 환자 이름 검색
    - patientRegistrationNumber (optional): 환자등록번호 검색
    - birthDay (optional): 생년월일 검색
    - page (default: 1): 페이지 번호 (1부터 시작)
    - size (default: 10): 페이지 크기
    - sort (default: id,desc): 기본 정렬 조건

## 5) 엔티티 설계
<details>
<summary><strong>Hospital Entity</strong></summary>

**속성**
- `id`: `Long` - 뱡원 식별자(기본 키)
- `hospitalName`: `String` 병원 이름
- `nursingInstitutionNumber`: `String` - 요양기관번호
- `hospitalDirector`: `String` - 병원장 이름
- `patients`: `MutableList<Patient>` -  해당 병원에 등록된 환자 목록 (1:N 관계, Hospital이 주인)
- `visits`: ` MutableList<Visit>` - 해당 병원에 대한 방문 기록 목록 (1:N 관계, Hospital이 주인)

**연관관계**
- Hospital ↔ Patient: 1:N 양방향 관계 (한 병원은 여러 환자를 가질 수 있음)
- Hospital ↔ Visit: 1:N 양방향 관계 (한 병원은 여러 방문 기록을 가질 수 있음)
</details>

<details>
<summary><strong>Patient Entity</strong></summary>

**속성**
- `id`: `Long` - 환자 식별자(기본 키)
- `patientName`: `String` - 환자 이름
- `patientRegistrationNumber`: `String` - 환자 등록 번호(고유값)
- `genderCode`: String - 성별 코드
- `birthDay`: `LocalDate?` - 생년월일
- `phoneNumber`: `String?` - 전화번호
- `hospital`: `Hospital` - 환자가 등록된 병원 (N:1 관계, Patient가 종속됨)
- `visits`: `MutableList<Visit>` - 환자의 방문 기록 목록 (1:N 관계, Patient가 주인)

**연관관계**
- Patient ↔ Hospital: N:1 양방향 관계 (여러 환자가 하나의 병원에 속함)
- Patient ↔ Visit: 1:N 양방향 관계 (한 환자는 여러 방문 기록을 가질 수 있음)

**행위**
- `companion object의 create() 팩토리 함수`: 환자 정보를 등록합니다. 병원별로 중복되지 않는 환자등록번호를 자동 생성합니다.
- `update()`: 환자 정보를 수정합니다.

</details>

<details>
<summary><strong>Visit Entity</strong></summary>

**속성**
- `id`: `Long?` - 환자방문 식별자(기본 키)
- `reservationDate`: `LocalDate` - 예약 날짜
- `visitStateCode`: `String` - 방문 상태 코드
- `hospital`: `Hospital` - 방문한 병원 (N:1 관계, Visit이 종속됨)
- `patient`: `Patient` - 방문한 환자 (N:1 관계, Visit이 종속됨)

**연관관계**
- Visit ↔ Hospital: N:1 양방향 관계 (여러 방문 기록이 하나의 병원에 속함)
- Visit ↔ Patient: N:1 양방향 관계 (여러 방문 기록이 하나의 환자에 속함)

**행위**
- `companion object의 create() 팩토리 함수` - 환자방문 정보를 등록합니다.
- `update()`: 환자방문 정보를 수정합니다.
</details>

<details>
<summary><strong>CodeGroup Entity</strong></summary>

**속성**
- `codeGroup`: `String` - 코드 그룹 식별자(기본 키)
- `codeGroupName`: `String` - 코드 그룹 이름
- `description`: String - 코드 그룹에 대한 설명
- `codes`: `MutableList<Code>` - 해당 코드 그룹에 속한 코드 목록

**연관관계**
- CodeGroup ↔ Code: 1:N 양방향 관계 (하나의 코드 그룹은 여러 코드를 가짐)
</details>

<details>
<summary><strong>Code Entity</strong></summary>

**속성**
- `id`: `CodeId` - 복합 키(codeGroup + code)
- `codeName`: `String` - 코드 이름
- `codeGroup`: `CodeGroup` - 코드가 속한 그룹

**연관관계**
- CodeGroup ↔ Code: 1:N 양방향 관계 (하나의 코드 그룹은 여러 코드를 가짐)
</details>

<details>
<summary><strong>CodeId (복합 키 클래스)</strong></summary>

**속성**
- `codeGroup`: `String` - 코드 그룹 식별자
- `code`: `String` - 코드 값
</details>



## 6) 추가 구성 요소
- QueryDSL: 동적 검색 및 페이징을 위한 구현
- Validation:
  - 코틀린에서는 기존에 `require`를 사용하여 검증을 수행하였으나, 향후에는 스프링의 벨리데이션 기능을 이용해서 Controller 단에서 DTO 검증을 처리하는 방식으로 변경할 수 있습니다.
  - 테스트 코드에서 ValidationI(ValidatorFactory, Validator)를 활용하여 requestDto의 유효성을 검증하고 있습니다.
- 테스트: 단위 테스트 및 통합 테스트 (JUnit5, Spring Test)