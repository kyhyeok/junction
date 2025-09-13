package hd.junction.patient.dto.response

import java.time.LocalDate

data class PatientPageResponseDto(
    val id: Long,
    val patientName: String,
    val patientRegistrationNumber: String,
    val genderCode: String,
    private val _birthDay: LocalDate?,
    private val _phoneNumber: String?,
    private val _visitDate: LocalDate?,
) {
    val birthDay: String get() = _birthDay?.toString() ?: "-"
    val phoneNumber: String get() = _phoneNumber ?: "-"
    val visitDate: String get() = _visitDate?.toString() ?: "-"
}