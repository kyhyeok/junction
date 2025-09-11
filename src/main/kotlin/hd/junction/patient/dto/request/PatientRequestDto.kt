package hd.junction.patient.dto.request

import java.time.LocalDate

data class PatientCreateRequestDto(
    val hospitalId: Long,
    val patientName: String,
    val patientRegistrationNumber: String,
    val genderCode: String,
    val birthDay: LocalDate?,
    val phoneNumber: String?,
)
