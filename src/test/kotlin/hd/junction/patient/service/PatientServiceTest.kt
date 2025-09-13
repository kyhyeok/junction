package hd.junction.patient.service

import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.fixture.PatientFixture.testPatientCreateRequestFixture
import hd.junction.patient.fixture.PatientFixture.testPatientSearchRequestDtoFixture
import hd.junction.patient.infrastructure.PatientRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["test"])
class PatientServiceTest @Autowired constructor(
    private val patientService: PatientService,
    private val patientRepository: PatientRepository,
    private val hospitalRepository: HospitalRepository,
) {

    @Test
    @Transactional
    @DisplayName("환자 등록 성공")
    fun createPatient_When_AllConditionsAreMet() {
        // given
        val request = testPatientCreateRequestFixture()

        // when
        val savedPatient = patientService.createPatient(request)

        // then
        with(savedPatient) {
            assertThat(id).isNotNull
            assertThat(patientName).isEqualTo(savedPatient.patientName)
            assertThat(patientRegistrationNumber).isEqualTo(savedPatient.patientRegistrationNumber)
            assertThat(genderCode).isEqualTo(savedPatient.genderCode)
            assertThat(birthDay).isEqualTo(savedPatient.birthDay)
            assertThat(phoneNumber).isEqualTo(savedPatient.phoneNumber)

            with(hospital) {
                assertThat(id).isEqualTo(savedPatient.hospital.id)
                assertThat(hospitalName).isEqualTo(savedPatient.hospital.hospitalName)
                assertThat(nursingInstitutionNumber).isEqualTo(savedPatient.hospital.nursingInstitutionNumber)
                assertThat(hospitalDirector).isEqualTo(savedPatient.hospital.hospitalDirector)
            }
        }

        patientRepository.deleteById(savedPatient.id)
    }

    @Test
    @DisplayName("환자 등록 실패 - 병원 없음")
    fun createPatient_When_HospitalNotFound() {
        // given
        val request = testPatientCreateRequestFixture(hospitalId = 1_000_000_000L)

        // when && then
        assertThatThrownBy { patientService.createPatient(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("확인되지 않은 병원 정보입니다")
    }

    @Test
    @Transactional
    @DisplayName("환자 수정 성공")
    fun updatePatient_When_AllConditionsAreMet() {
        // given
        val createRequest = testPatientCreateRequestFixture()
        val savedPatient = patientService.createPatient(createRequest)

        val updateRequest = testPatientCreateRequestFixture(
            hospitalId = 2,
            patientName = "이환자 수정",
            phoneNumber = "010-2222-2222"
        )


        // when
        val updatedPatient = patientService.updatePatient(savedPatient.id, updateRequest)

        // then
        with(updatedPatient) {
            assertThat(id).isNotNull
            assertThat(patientName).isEqualTo(updatedPatient.patientName)
            assertThat(patientRegistrationNumber).isEqualTo(updatedPatient.patientRegistrationNumber)
            assertThat(genderCode).isEqualTo(updatedPatient.genderCode)
            assertThat(birthDay).isEqualTo(updatedPatient.birthDay)
            assertThat(phoneNumber).isEqualTo(updatedPatient.phoneNumber)

            with(hospital) {
                assertThat(id).isEqualTo(updatedPatient.hospital.id)
                assertThat(hospitalName).isEqualTo(updatedPatient.hospital.hospitalName)
                assertThat(nursingInstitutionNumber).isEqualTo(updatedPatient.hospital.nursingInstitutionNumber)
                assertThat(hospitalDirector).isEqualTo(updatedPatient.hospital.hospitalDirector)
            }
        }

        patientRepository.deleteById(updatedPatient.id)
    }

    @Test
    @DisplayName("환자 수정 실패 - 환자 없음")
    fun updatePatient_When_PatientNotFound() {
        // given
        val request = testPatientCreateRequestFixture()

        // when && then
        assertThatThrownBy { patientService.updatePatient(1_000_000_000L, request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("확인되지 않은 환자입니다")
    }

    @Test
    @Transactional
    @DisplayName("환자 삭제 성공")
    fun deletePatient() {
        // given
        val request = testPatientCreateRequestFixture()
        val savedPatient = patientService.createPatient(request)
        assertThat(savedPatient).isNotNull()

        // when
        patientService.deletePatient(savedPatient.id)

        // then
        assertThat(patientRepository.existsById(savedPatient.id)).isFalse()
    }

    @Test
    @DisplayName("환자 자세히 조회 성공 - 방문 이력 포함")
    fun getPatientDetail_When_Visited() {
        // given
        val patientId = 1L

        // when
        val patientDetail = patientService.getPatientDetail(patientId)

        // then
        with(patientDetail) {
            assertThat(id).isEqualTo(patientDetail.id)
            assertThat(patientName).isEqualTo(patientDetail.patientName)
            assertThat(patientRegistrationNumber).isEqualTo(patientDetail.patientRegistrationNumber)
            assertThat(genderCode).isEqualTo(patientDetail.genderCode)
            assertThat(birthDay).isEqualTo(patientDetail.birthDay)
            assertThat(phoneNumber).isEqualTo(patientDetail.phoneNumber)

            assertThat(patientDetail.visits).isNotEmpty()

            patientDetail.visits.forEach { visit ->
                with(visit) {
                    assertThat(id).isEqualTo(visit.id)
                    assertThat(reservationDate).isEqualTo(visit.reservationDate)
                    assertThat(visitStateCode).isEqualTo(visit.visitStateCode)
                    assertThat(visit.hospital).isNotNull

                    with(visit.hospital) {
                        assertThat(id).isEqualTo(visit.hospital.id)
                        assertThat(hospitalName).isEqualTo(visit.hospital.hospitalName)
                        assertThat(nursingInstitutionNumber).isEqualTo(visit.hospital.nursingInstitutionNumber)
                        assertThat(hospitalDirector).isEqualTo(visit.hospital.hospitalDirector)
                    }
                }
            }
        }
    }

    @Test
    @Transactional
    @DisplayName("환자 자세히 조회 성공 - 방문 이력 없음")
    fun getPatientDetail_When_NotVisited() {
        // given
        val request = testPatientCreateRequestFixture()
        val savedPatient = patientService.createPatient(request)

        // when
        val patientDetail = patientService.getPatientDetail(savedPatient.id)

        // then
        with(patientDetail) {
            assertThat(id).isEqualTo(patientDetail.id)
            assertThat(patientName).isEqualTo(patientDetail.patientName)
            assertThat(patientRegistrationNumber).isEqualTo(patientDetail.patientRegistrationNumber)
            assertThat(genderCode).isEqualTo(patientDetail.genderCode)
            assertThat(birthDay).isEqualTo(patientDetail.birthDay)
            assertThat(phoneNumber).isEqualTo(patientDetail.phoneNumber)

            assertThat(patientDetail.visits).isEmpty()
        }

        patientRepository.deleteById(savedPatient.id)
    }

    @Test
    @DisplayName("환자 목록 조회 - 환자 이름 검색")
    fun getPatientWithPage_When_SearchPatientName() {
        // given
        val requestDto = testPatientSearchRequestDtoFixture(patientName = "김환자1")
        val pageable = PageRequest.of(0, 10)

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then
        foundPatients.forEach { patient ->
            with(patient) {
                assertThat(patientName).contains("김환자1")
            }
        }
    }

    @Test
    @DisplayName("환자 목록 조회 - 환자 등록 번호 검색")
    fun getPatientWithPage_When_SearchPatientRegistrationNumber() {
        // given
        val requestDto = testPatientSearchRequestDtoFixture(patientRegistrationNumber = "1122331122331")
        val pageable = PageRequest.of(0, 10)

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then
        foundPatients.forEach { patient ->
            with(patient) {
                assertThat(patientRegistrationNumber).contains("1122331122331")
            }
        }
    }

    @Test
    @DisplayName("환자 목록 조회 - 생년월일 검색")
    fun getPatientWithPage_When_SearchBirthDay() {
        // given
        val date = LocalDate.of(1990, 1, 1)
        val requestDto = testPatientSearchRequestDtoFixture(birthDay = date)
        val pageable = PageRequest.of(0, 10)

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then
        foundPatients.forEach { patient ->
            with(patient) {
                assertThat(birthDay).contains("1990-01-01")
            }
        }
    }

    @Test
    @DisplayName("환자 목록 조회 - null 검증")
    fun getPatientWithPage_When_PhoneNumberIsNull_Then_DisplaysDash() {
        // 테스트 데이터에 birthDay, phoneNumber, visitDate null인 환자가 최소 한 명 있어야 함

        // given
        val requestDto = testPatientSearchRequestDtoFixture()
        val pageable = PageRequest.of(0, 20_000) // 충분히 큰 값으로 모든 null값 포함 환자 조회 가능

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then birthDay
        val patientsWithNullBirthday = foundPatients.filter { it.birthDay == "-" }
        assertThat(patientsWithNullBirthday).isNotEmpty()
        patientsWithNullBirthday.forEach { assertThat(it.birthDay).isEqualTo("-") }


        // then phoneNumber
        val patientsWithNullPhone = foundPatients.filter { it.phoneNumber == "-" }
        assertThat(patientsWithNullPhone).isNotEmpty()
        patientsWithNullPhone.forEach { assertThat(it.phoneNumber).isEqualTo("-") }


        // then visitDate
        val patientsWithNullVisitDate = foundPatients.filter { it.visitDate == "-" }
        assertThat(patientsWithNullVisitDate).isNotEmpty()
        patientsWithNullVisitDate.forEach { assertThat(it.visitDate).isEqualTo("-") }
    }


    @Test
    @DisplayName("환자 목록 조회 - 첫 페이지, 10개 페이징, id 오름차순 정렬")
    fun getPatientWithPage_When_PagingFirst10() {
        // given
        val pageNumber = 10
        val totalCount = patientRepository.count()
        val expectedTotalPages = if (totalCount % pageNumber == 0L) totalCount / pageNumber else (totalCount / pageNumber) + 1
        val requestDto = testPatientSearchRequestDtoFixture()
        val pageable = PageRequest.of(0, pageNumber, Sort.by(Sort.Direction.ASC, "id"))

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then
        assertThat(foundPatients.content).hasSize(pageNumber)
        assertThat(foundPatients.number).isEqualTo(0)
        assertThat(foundPatients.size).isEqualTo(pageNumber)
        assertThat(foundPatients.totalElements).isEqualTo(totalCount)
        assertThat(foundPatients.totalPages).isEqualTo(expectedTotalPages)
        assertThat(foundPatients.isFirst).isTrue()
        assertThat(foundPatients.isLast).isFalse()
        assertThat(foundPatients.numberOfElements).isEqualTo(pageNumber)
        assertThat(foundPatients.sort.isSorted).isTrue()
    }

    @Test
    @DisplayName("환자 목록 조회 - 세 번째 페이지, 10개 페이징, id 오름차순 정렬")
    fun getPatientWithPage_When_PagingThird10() {
        // given
        val pageNumber = 10
        val totalCount = patientRepository.count()
        val expectedTotalPages = if (totalCount % pageNumber == 0L) totalCount / pageNumber else (totalCount / pageNumber) + 1
        val requestDto = testPatientSearchRequestDtoFixture()
        val pageable = PageRequest.of(2, pageNumber, Sort.by(Sort.Direction.ASC, "id"))

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)

        // then
        assertThat(foundPatients.content).hasSize(pageNumber)
        assertThat(foundPatients.number).isEqualTo(2)
        assertThat(foundPatients.size).isEqualTo(pageNumber)
        assertThat(foundPatients.totalElements).isEqualTo(totalCount)
        assertThat(foundPatients.totalPages).isEqualTo(expectedTotalPages)
        assertThat(foundPatients.isFirst).isFalse()
        assertThat(foundPatients.isLast).isFalse()
        assertThat(foundPatients.numberOfElements).isEqualTo(pageNumber)
        assertThat(foundPatients.sort.isSorted).isTrue()
    }

    @Test
    @DisplayName("환자 목록 조회 - 마지막 페이지")
    fun getPatientWithPage_When_LastPaging() {
        // given
        val pageNumber = 10
        val totalCount = patientRepository.count()
        val lastPageNumber = (totalCount / pageNumber).toInt()
        val pageNumberRemain = (totalCount % pageNumber).toInt()
        val expectedTotalPages = if (totalCount % pageNumber == 0L) totalCount / pageNumber else (totalCount / pageNumber) + 1
        val requestDto = testPatientSearchRequestDtoFixture()
        val pageable = PageRequest.of(lastPageNumber, pageNumber, Sort.by(Sort.Direction.ASC, "id"))

        // when
        val foundPatients = patientService.getPatientWithPage(requestDto, pageable)
        println(foundPatients)

        // then
        assertThat(foundPatients.number).isEqualTo(lastPageNumber)
        assertThat(foundPatients.size).isEqualTo(pageNumber)
        assertThat(foundPatients.totalElements).isEqualTo(totalCount)
        assertThat(foundPatients.totalPages).isEqualTo(expectedTotalPages)
        assertThat(foundPatients.isFirst).isFalse()
        assertThat(foundPatients.isLast).isTrue()
        assertThat(foundPatients.numberOfElements).isEqualTo(pageNumberRemain)
        assertThat(foundPatients.sort.isSorted).isTrue()
    }
}