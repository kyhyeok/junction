package hd.junction.patient.dto.request

import hd.junction.patient.fixture.PatientFixture.testPatientCreateRequestFixture
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientRequestDtoTest {
    private lateinit var factory: ValidatorFactory
    private lateinit var validator: Validator

    @BeforeAll
    fun initValidator() {
        factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @AfterAll
    fun closeValidator() {
        factory.close()
    }

    @Test
    @DisplayName("PatientRequestDto - 유효한 값으로 PatientRequestDto 생성")
    fun patientRequestDto_When_AllFieldsAreValid() {
        // given
        val request = testPatientCreateRequestFixture()

        // when
        val violations = validator.validate(request)

        // testPatientCreateRequestFixture() 기본값 수정 시 아래 값도 함께 수정 필요
        // then
        assertThat(violations).isEmpty()
        with(request) {
            assertThat(patientName).isEqualTo("김환자")
            assertThat(patientRegistrationNumber).isEqualTo("1234567899992")
            assertThat(genderCode).isEqualTo("F")
            assertThat(birthDay).isEqualTo(LocalDate.of(1992, 5, 16))
            assertThat(phoneNumber).isEqualTo("010-1111-1111")
            assertThat(hospitalId).isEqualTo(1L)
        }
    }

    @Test
    @DisplayName("PatientRequestDto - 병원 ID는 0보다 커야 합니다")
    fun patientRequestDto_When_HospitalIdIsNegative() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(hospitalId = -1)
        }

        assertThat(exception.message).isEqualTo("병원 ID는 0보다 커야 합니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 환자 이름은 필수 입력 값입니다")
    fun patientRequestDto_When_PatientNameIsBlank() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(patientName = "")
        }

        assertThat(exception.message).isEqualTo("환자 이름은 필수 입력 값입니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 환자 이름은 최대 45자까지 입력 가능합니다")
    fun patientRequestDto_When_PatientNameIsTooLong() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(patientName = "x".repeat(46))
        }

        assertThat(exception.message).isEqualTo("환자 이름은 최대 45자까지 입력 가능합니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 환자 등록 번호는 필수 입력 값입니다")
    fun patientRequestDto_When_RegistrationNumberIsBlank() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(patientRegistrationNumber = "")
        }

        assertThat(exception.message).isEqualTo("환자 등록 번호는 필수 입력 값입니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 환자 등록 번호는 최대 13자까지 입력 가능합니다")
    fun patientRequestDto_When_RegistrationNumberIsTooLong() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(patientRegistrationNumber = "x".repeat(14))
        }

        assertThat(exception.message).isEqualTo("환자 등록 번호는 최대 13자까지 입력 가능합니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 성별 코드는 M(남성) 또는 F(여성)만 입력 가능합니다")
    fun patientRequestDto_When_GenderCodeIsInvalid() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(genderCode = "Z")
        }

        assertThat(exception.message).isEqualTo("성별 코드는 M(남성) 또는 F(여성)만 입력 가능합니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 생년월일은 과거 날짜만 가능합니다")
    fun patientRequestDto_When_BirthDayIsFuture() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(birthDay = LocalDate.now().plusDays(1))
        }

        assertThat(exception.message).isEqualTo("생년월일은 과거 날짜만 가능합니다")
    }

    @Test
    @DisplayName("PatientRequestDto - 휴대폰 번호는 최대 20자까지 입력 가능합니다")
    fun patientRequestDto_When_PhoneNumberIsTooLong() {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(phoneNumber = "010-1234-5678-1111-2222")
        }

        assertThat(exception.message).isEqualTo("휴대폰 번호는 최대 20자까지 입력 가능합니다")
    }

    @ParameterizedTest
    @DisplayName("PatientRequestDto - 휴대폰 번호 형식이 올바르지 않습니다")
    @ValueSource(
        strings = [
            "010-123-5678",        // 중간자리 3자리
            "010-12345-678",       // 중간자리 5자리
            "010-1234-567",        // 뒷자리 3자리
            "010-1234-56789",      // 뒷자리 5자리
            "0101234567",          // 11자리 (010 + 7자리)
            "010123456789",        // 13자리
            "010-12-34-5678",      // 하이픈 위치 잘못
            "010/1234/5678",       // 슬래시 사용
            "010.1234.5678",       // 점 사용
            "010 1234 5678",       // 공백 사용
            "010-abcd-5678",       // 문자 포함
            "010-1234-abcd",       // 문자 포함
            "abc-1234-5678",       // 앞자리 문자
            "1234-5678",           // 앞자리 누락
            "010-1234",            // 뒷자리 누락
            "",                    // 빈 문자열
            "010",                 // 너무 짧음
        ]
    )
    fun patientRequestDto_When_PhoneNumberFormatIsInvalid(
        invalidPhoneNumber: String
    ) {
        // given & when & then
        val exception = assertThrows<IllegalArgumentException> {
            testPatientCreateRequestFixture(phoneNumber = invalidPhoneNumber)
        }

        assertThat(exception.message).isEqualTo("휴대폰 번호 형식이 올바르지 않습니다. 예: 010-1234-5678 or 01012345678")
    }
}