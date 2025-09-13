package hd.junction.patient.dto.response

import hd.junction.patient.domain.Patient
import hd.junction.visit.domain.Visit
import hd.junction.visit.dto.response.VisitHospitalResponseDto
import java.time.LocalDate

data class PatientVisitResponseDto(
    val id: Long,
    val patientName: String,
    val patientRegistrationNumber: String,
    val genderCode: String,
    val birthDay: LocalDate?,
    val phoneNumber: String?,
    val visits: List<VisitHospitalResponseDto>,
) {
    companion object {
        fun of(patient: Patient, visits: MutableList<Visit>): PatientVisitResponseDto {
            val visitHospitalResponses = visits.map { visit -> VisitHospitalResponseDto.of(visit) }

            return PatientVisitResponseDto(
                id = patient.id!!,
                patientName = patient.patientName,
                patientRegistrationNumber = patient.patientRegistrationNumber,
                genderCode = patient.genderCode,
                birthDay = patient.birthDay,
                phoneNumber = patient.phoneNumber,
                visits = visitHospitalResponses
            )
        }
    }
}
