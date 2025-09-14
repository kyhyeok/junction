package hd.junction.visit.dto.request

import hd.junction.patient.dto.request.VisitPatientRequestDto
import java.time.LocalDate

data class VisitCreateRequestDto(
    val reservationDate: LocalDate,

    val visitStateCode: String,

    val hospitalId: Long,

    val patientId: Long? = null,

    val patientRequest: VisitPatientRequestDto? = null,
) {
    init {
        require(patientId != null || patientRequest != null) {
            "환자 ID 또는 환자 정보 중 하나는 반드시 제공되어야 합니다"
        }
    }
}

data class VisitUpdateRequestDto(
    val reservationDate: LocalDate,

    val visitStateCode: String
) 