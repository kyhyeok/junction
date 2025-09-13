package hd.junction.visit.dto.response

import hd.junction.hospital.dto.response.HospitalResponseDto
import hd.junction.visit.domain.Visit
import java.time.LocalDate

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
