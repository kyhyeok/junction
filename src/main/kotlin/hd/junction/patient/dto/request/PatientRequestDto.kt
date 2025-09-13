package hd.junction.patient.dto.request

import java.time.LocalDate

data class PatientRequestDto(
    val hospitalId: Long,
    val patientName: String,
    val genderCode: String,
    val birthDay: LocalDate?,
    val phoneNumber: String?,
) {
    init {
        require(hospitalId > 0) { "병원 ID는 0보다 커야 합니다" }
        require(patientName.isNotBlank()) { "환자 이름은 필수 입력 값입니다" }
        require(patientName.length <= 45) { "환자 이름은 최대 45자까지 입력 가능합니다" }
        require(genderCode in listOf("M", "F")) { "성별 코드는 M(남성) 또는 F(여성)만 입력 가능합니다" }
        birthDay?.let { birth ->
            require(birth.isBefore(LocalDate.now())) { "생년월일은 과거 날짜만 가능합니다" }
        }
        phoneNumber?.let { phone ->
            require(phone.length <= 20) { "휴대폰 번호는 최대 20자까지 입력 가능합니다" }
            require(isValidPhoneNumber(phone)) { "휴대폰 번호 형식이 올바르지 않습니다. 예: 010-1234-5678 or 01012345678" }
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return Regex("^010-?[0-9]{4}-?[0-9]{4}$").matches(phone)
    }
}
