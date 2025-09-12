package hd.junction.patient.service

import hd.junction.patient.fixture.PatientFixture.testPatientCreateRequestFixture
import hd.junction.patient.infrastructure.PatientRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = ["test"])
class PatientServiceTest @Autowired constructor(
    private val patientService: PatientService,
    private val patientRepository: PatientRepository
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
    @DisplayName("환자 등록 실패 - 같은 병원에서의 환자 등록 번호 중복")
    fun createPatient_When_PatientINHospital() {
        // given
        val request = testPatientCreateRequestFixture()

        val savedPatient = patientService.createPatient(request)

        // when && then
        assertThatThrownBy { patientService.createPatient(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("${savedPatient.hospital.hospitalName}에 이미 등록된 환자입니다")

        patientRepository.deleteById(savedPatient.id)
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
}