package hd.junction.patient.fixture

import hd.junction.common.util.RandomUtils.generateRandomPatientRegistrationNumber
import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.request.PatientSearchRequestDto
import java.time.LocalDate

object PatientFixture {
    fun testPatientCreateRequestFixture(
        hospitalId: Long = 1L,
        patientName: String = "김환자",
        genderCode: String = "F",
        birthDay: LocalDate? = LocalDate.of(1992, 5, 16),
        phoneNumber: String? = "010-1111-1111",
    ): PatientRequestDto {
        return PatientRequestDto(
            hospitalId = hospitalId,
            patientName = patientName,
            genderCode = genderCode,
            birthDay = birthDay,
            phoneNumber = phoneNumber,
        )
    }

    fun testPatientSearchRequestDtoFixture(
        patientName: String? = null,
        patientRegistrationNumber: String? = null,
        birthDay: LocalDate? = null,
    ): PatientSearchRequestDto {
        return PatientSearchRequestDto(
            patientName = patientName,
            patientRegistrationNumber = patientRegistrationNumber,
            birthDay = birthDay,
        )
    }
}
