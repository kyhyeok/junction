package hd.junction.patient.fixture

import hd.junction.patient.dto.request.PatientCreateRequestDto
import java.time.LocalDate

object PatientFixture {
    fun testPatientCreateRequestFixture(
        hospitalId: Long = 1L,
        patientName: String = "김환자",
        patientRegistrationNumber: String = "1234567899992",
        genderCode: String = "F",
        birthDay: LocalDate? = LocalDate.of(1992, 5, 16),
        phoneNumber: String? = "010-1111-1111",
    ): PatientCreateRequestDto {
        return PatientCreateRequestDto(
            hospitalId = hospitalId,
            patientName = patientName,
            patientRegistrationNumber = patientRegistrationNumber,
            genderCode = genderCode,
            birthDay = birthDay,
            phoneNumber = phoneNumber,
        )
    }
}
