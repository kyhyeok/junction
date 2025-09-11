package hd.junction.patient.service

import hd.junction.patient.fixture.PatientFixture.testPatientCreateRequestFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["test"])
class PatientServiceTest @Autowired constructor(
    private val patientService: PatientService
) {
    @Test
    @DisplayName("환자 등록 성공")
    fun createPatientSuccess() {
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
                assertThat(id).isNotNull
                assertThat(hospitalName).isEqualTo(savedPatient.hospital.hospitalName)
                assertThat(nursingInstitutionNumber).isEqualTo(savedPatient.hospital.nursingInstitutionNumber)
                assertThat(hospitalDirector).isEqualTo(savedPatient.hospital.hospitalDirector)
            }
        }
    }

    @Test
    @DisplayName("환자 등록 실패 - 병원 없음")
    fun createPatientFail_When_HospitalNotFound() {
        // given
        val request = testPatientCreateRequestFixture(hospitalId = 1_000_000_000L)

        // when && then
        assertThatThrownBy { patientService.createPatient(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("확인되지 않은 병원 정보입니다")
    }

    @Test
    @DisplayName("환자 등록 실패 - 성별 코드 없음")
    fun createPatientFail_When_GenderCodeNotFound() {
        // given
        val request = testPatientCreateRequestFixture(genderCode = "Z")

        // when && then
        assertThatThrownBy { patientService.createPatient(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("성별 입력 코드를 확인해주세요")
    }
}