### 컨트롤러 선행 작업 (애플리케이션 정상 동작 여부 확인을 하기 위해서)

- [x] VisitController, PatientController
- [x] 통신 기능만 확인 (.http 파일로 대체)

### Entity 작업

- [x] Patient, Hospital, Visit (연관관계 매핑하기, 데이터베이스 확인)
- [x] CodeGroup, Codes (연관관계 매핑하기, 데이터베이스 확인)

### Repository 작업

- [x] PatientRepository, HospitalRepository, VisitRepository

### 환자 등록 작업을 하기 위한 선행 작업

- [x] CodesRepository 구현 작업
- [x] 환자의 성별코드 입력을 위한 코드그룹, 코드는 data.sql로 insert 작업
- [x] 환자의 병원ID 입력을 위한 병원 데이터 insert 작업
- [x] 환자 service에서 코드와 병원 데이터 확인하기

### 환자 등록 작업

- [x] PatientRequestDto 구현
- [x] PatientService.createPatient() 메서드 구현
- [x] PatientController.createPatient() 메서드 구현
- [x] PatientHospitalResponseDto, HospitalResponseDto 구현
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

- [x] 병원, 환자, 환자방문 테스트 데이터 dataInitializer 작업
- [x] PatientHospitalResponseDto 구현
- [x] PatientService.getPatientDetail() 메서드 구현
- [x] PatientController.getPatientDetail() 메서드 구현
- [x] 테스트 코드 작성 및 http 파일로 테스트
- [x] dataInitializer 수정 및 리팩토링 작업

### 환자 목록 조회 작업

- [x] QueryDsl gradle 의존성, queryDslConfig, queryDslRepository, Patient Index 구현
- [x] PatientSearchRequestDto, PatientPageResponseDto 구현
- [x] PatientService.getPatientWithPage() 메서드 구현
- [x] PatientController.getPatientWithPage() 메서드 구현
- [x] 동적 검색 조건 작업, dataInitializer 생년월일, 휴대전화 null 저장 작업
- [x] 테스트 코드 작성 및 dataInitializer 환자 등록 번호, 생년월일 고장 값 저장 작업
- [x] PatientPageResponseDto null 처리 작업
- [x] 코드 다듬기
- [x] 페이징 작업
- [x] 테스트 코드 작성 및 http 파일로 테스트, 리팩토링


### 전체 소스코드 확인 및 추가 / 변경 작업
- [x] controller 반환값 ResponseEntity으로 변경 작업
- [x] Patient의 patientRegistrationNumber request에서 받는 것을 내부에서 생성하는 것으로 변경 작업
- [x] generateRandomPatientRegistrationNumber() 함수 구현
- [x] validatedPatientInHospital()에서 getUniquePatientRegistrationNumber()로 변경 (중복 체크 X loop로 중복 안되는 새로운 번호 생성)
- [x] 파일정리, 오타수정, CHANGELOG.md(작업 내용 기록)
- [x] 페이지 번호 1번부터 시작하도록 변경 작업, PatientController 테스트 코드 작성, 소스코드 다듬기



### 환자 방문 작업
- [x] VisitController, VisitService, DTO 환자방문 등록 작업
- [x] PatientRequestDto 공통 로직 구현 및 리팩토링 작업
- [x] VisitController, VisitService, DTO 환자방문 수정, 삭제, 조회 작업

### README.md 작성
- [x] README.md 작성 및 visit Controller, visit Service 코드 다듬기