# 에이치디정션 백엔드 개발자 채용 온라인 과제

## 병원과 환자 프로젝트
- 병원과 환자의 방문 이력을 관리하는 시스템

### 프로젝트 세팅하기
- Java 21, Spring Boot 3.4.9
- Gradle 8.14
- Spring Data JPA + QueryDSL
- Spring Web
- H2 Database
- Validation
- application.yml 설정

### 컨트롤러 선행 작업 (애플리케이션 정상 동작 여부 확인을 하기 위해서)
- [] VisitController, PatientController
- [] 통신 기능만 확인

### Entity 작업
- [] Hospital, Patient, Visit
- [] CodeGroup, Codes
- [] 연관관계 매핑하기
- [] 데이터베이스 확인

### Repository 작업
- [] PatientRepository, VisitRepository
- [] PatientRepository QueryDsl, queryDslConfig


### Service 작업
- [] PatientService
- [] VisitService

### DTO 작업
- [] Request (Validation 작업 포함)
  - [] PatientCreateRequestDto
  - [] PatientUpdateRequestDto
  - [] PatientSearchRequestDto
  - [] VisitCreateRequestDto
  - [] VisitUpdateRequestDto
  - [] RestControllerAdvice
- [] Response
  - [] PatientResponseDto
  - [] PatientPageResponseDto
  - [] VisitResponseDto

### Controller 마무리 작업
- [] VisitController, PatientController

### 테스트 코드 작성
- [] 테스트를 위한 데이터 insert
- [] PatientRequestDtoTest
- [] PatientServiceTest
- [] PatientControllerTest (.http 파일로 대체)

### 리팩토링 작업