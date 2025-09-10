package hd.junction.hospital.dto.response

import hd.junction.hospital.domain.Hospital

data class HospitalResponseDto(
    val hospitalName: String,
    val nursingInstitutionNumber: String,
    val hospitalDirector: String,
    val id: Long,
) {
    companion object {
        fun of(hospital: Hospital): HospitalResponseDto {
            return HospitalResponseDto(
                id = hospital.id!!,
                hospitalName = hospital.hospitalName,
                nursingInstitutionNumber = hospital.nursingInstitutionNumber,
                hospitalDirector = hospital.hospitalDirector
            )
        }
    }
}