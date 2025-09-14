package hd.junction.patient.dto.response

import hd.junction.hospital.dto.response.HospitalResponseDto
import hd.junction.patient.domain.Patient
import java.time.LocalDate

data class PatientResponseDto(
    val id: Long,
    val patientName: String,
    val patientRegistrationNumber: String,
    val genderCode: String,
    val birthDay: LocalDate?,
    val phoneNumber: String?,
) {
    companion object {
        fun of (patient: Patient): PatientResponseDto {
            return PatientResponseDto(
                id = patient.id!!,
                patientName = patient.patientName,
                patientRegistrationNumber = patient.patientRegistrationNumber,
                genderCode = patient.genderCode,
                birthDay = patient.birthDay,
                phoneNumber = patient.phoneNumber,
            )
        }
    }
}

data class PatientHospitalResponseDto(
    val id: Long,
    val patientName: String,
    val patientRegistrationNumber: String,
    val genderCode: String,
    val birthDay: LocalDate?,
    val phoneNumber: String?,
    val hospital: HospitalResponseDto,
) {
    companion object {
        fun of (patient: Patient): PatientHospitalResponseDto {
            return PatientHospitalResponseDto(
                id = patient.id!!,
                patientName = patient.patientName,
                patientRegistrationNumber = patient.patientRegistrationNumber,
                genderCode = patient.genderCode,
                birthDay = patient.birthDay,
                phoneNumber = patient.phoneNumber,
                hospital = HospitalResponseDto.of(patient.hospital)
            )
        }
    }
}
