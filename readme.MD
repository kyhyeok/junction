# 에이치디정션 백엔드 개발자 채용 온라인 과제


## 병원과 환자 프로젝트
- 병원과 환자 관리 및 환자의 병원 방문 이력 관리 시스템


### 프로젝트 세팅하기
- Java 21, Spring Boot 3.4.9
- Gradle 8.14
- Spring Data JPA + QueryDSL
- Spring Web
- H2 Database
- Validation
- application.yml 설정


### 컨트롤러 선행 작업 (애플리케이션 정상 동작 여부 확인을 하기 위해서)
- [x] VisitController, PatientController
- [x] 통신 기능만 확인 (.http 파일로 대체)


### Entity 작업
- [x] Patient, Hospital, Visit (연관관계 매핑하기, 데이터베이스 확인)
- [x] CodeGroup, Codes (연관관계 매핑하기, 데이터베이스 확인)


### Repository 작업
- [x] PatientRepository, HospitalRepository, VisitRepository


### 환자 등록 작업을 하기 위한 선행 작업
- [x] codesRepository 구현 작업 
- [x] 환자의 성별코드 입력을 위한 코드그룹, 코드는 data.sql로 insert 작업
- [x] 환자의 병원ID 입력을 위한 병원 데이터 insert 작업
- [x] 환자 service에서 코드와 병원 데이터 확인하기


### 환자 등록 작업
- [x] PatientRequestDto 구현
- [x] PatientService.createPatient() 메서드 구현
- [x] PatientController.createPatient() 메서드 구현
- [x] PatientResponseDto, HospitalResponseDto 구현
- [x] 테스트 코드 작성 (성별 코드 오류, 병원 오류, 환자 저장)
- [x] 리팩토링 (Service, Entity, TestFixture)
- [x] PatientRequestDto 검증 require 구현
- [x] 환자등록번호 중복 처리 작업
- [x] 테스트 코드 작성 (request validation, 환자등록번호 종북)
- [x] 테스트 데이터 클리닝 작업


### 환자 수정 작업
- [x] PatientRequestDto
  - 기획서 확인 이후 환자 등록과 동일한 데이터 형태를 받아서 수정으로 이해. PatientCreateRequestDto를 그대로 받아서 사용
  - PatientCreateRequestDto를 PatientRequestDto로 클래스명 변경. 등록과 수정에 공통 사용
- [x] PatientEntity.update() 메서드 구현
- [x] PatientService.updatePatient() 메서드 구현
- [x] PatientController.updatePatient() 메서드 구현
- [x] 테스트 코드 작성 및 http 파일로 테스트
- [x] 리팩토링(성별코드 검증 함수화 작업)


### 환자 삭제 작업
- [x] PatientService.deletePatient() 메서드 구현
- [x] PatientController.deletePatient() 메서드 구현
- [x] 테스트 코드 및 http 파일로 테스트


### 환자 조회 작업
- [x] 병원, 환자, 환자방문 테스트 데이터 dateInitializer 작업
- [x] PatientResponseDto 구현
- [x] PatientService.getPatientDetail() 메서드 구현
- [x] PatientController.getPatientDetail() 메서드 구현
- [x] 테스트 코드 작성 및 http 파일로 테스트
- [x] dateInitializer 수정 및 리팩토링 작업


### 환자 목록 조회 작업
- [x] QueryDsl gradle 의존성, queryDslConfig, queryDslRepository, Patient Index 구현
- [] PatientSearchRequestDto 구현
- [] PatientPageResponseDto 구현
- [] PatientService.getPatientWithPage() 메서드 구현
- [] PatientController.getPatientWithPage() 메서드 구현
- [] 테스트 코드 작성 및 http 파일로 테스트


### 병원, 방문 등록 작업
- [] HospitalService, DTO 병원 등록 작업
- [] VisitService, DTO 환자 등록 작업


### 테스트 데이터 작업 및 API 작동 확인 작업
- [] PatientControllerTest.http로 API 확인 작업
  - [] 등록 확인 작업
  - [] 수정 확인 작업
  - [] 삭제 확인 작업
  - [] 상세 조회 확인 작업
  - [] 목록 조회 확인 작업 - 페이지네이션 
  - [] 목록 조회 확인 작업 - 환자이름 검색
  - [] 목록 조회 확인 작업 - 환자등록번호 검색
  - [] 목록 조회 확인 작업 - 생년월일 검색


### common 및 리팩토링