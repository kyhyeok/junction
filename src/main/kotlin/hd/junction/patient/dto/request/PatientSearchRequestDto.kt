package hd.junction.patient.dto.request

import java.time.LocalDate

data class PatientSearchRequestDto(
    val patientName: String?,
    val patientRegistrationNumber: String?,
    val birthDay: LocalDate?,
)
