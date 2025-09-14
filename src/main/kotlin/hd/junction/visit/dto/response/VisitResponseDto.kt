package hd.junction.visit.dto.response

import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.dto.response.HospitalResponseDto
import hd.junction.patient.domain.Patient
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.visit.domain.Visit
import java.time.LocalDate

data class VisitResponseDto(
    val id: Long,
    val reservationDate: LocalDate,
    val visitStateCode: String,
    val patients: PatientResponseDto,
    val hospitals: HospitalResponseDto,
) {
    companion object {
        fun of(visit: Visit): VisitResponseDto {
            return VisitResponseDto(
                id = visit.id!!,
                reservationDate = visit.reservationDate,
                visitStateCode = visit.visitStateCode,
                patients =  PatientResponseDto.of(visit.patient),
                hospitals = HospitalResponseDto.of(visit.hospital),
            )
        }
    }
}

data class VisitHospitalResponseDto(
    val id: Long,
    val reservationDate: LocalDate,
    val visitStateCode: String,
    val hospital: HospitalResponseDto,
) {
    companion object {
        fun of(visit: Visit): VisitHospitalResponseDto {
            return VisitHospitalResponseDto(
                id = visit.id!!,
                reservationDate = visit.reservationDate,
                visitStateCode = visit.visitStateCode,
                hospital = HospitalResponseDto.of(visit.hospital)
            )
        }
    }
}