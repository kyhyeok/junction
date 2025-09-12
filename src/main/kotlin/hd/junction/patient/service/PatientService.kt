package hd.junction.patient.service

import hd.junction.codes.infrastructure.CodeRepository
import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.infrastructure.PatientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private const val CODE_GROUP_GENDER: String = "성별코드"

@Service
class PatientService(
    private val codeRepository: CodeRepository,
    private val hospitalRepository: HospitalRepository,
    private val patientRepository: PatientRepository,
) {

    @Transactional
    fun createPatient(patientRequestDto: PatientRequestDto): PatientResponseDto {
        validatedGenderCode(patientRequestDto)

        val hospital = hospitalRepository.findById(patientRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        validatedPatientInHospital(hospital, patientRequestDto)

        return Patient.create(patientRequestDto, hospital)
            .let { patient -> patientRepository.save(patient) }
            .let { patient -> PatientResponseDto.of(patient) }
    }

    @Transactional
    fun updatePatient(id: Long, patientRequestDto: PatientRequestDto): PatientResponseDto {
        val foundPatient = patientRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 환자입니다") }

        validatedGenderCode(patientRequestDto)

        val hospital = hospitalRepository.findById(patientRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        validatedPatientInHospital(hospital, patientRequestDto)

        return foundPatient.update(patientRequestDto, hospital)
            .let { patient -> patientRepository.save(patient) }
            .let { patient -> PatientResponseDto.of(patient) }
    }


    //  PatientRequestDto에서 검증하지만, 혹시 모를 잘못된 코드값이 들어올 경우를 대비
    private fun validatedGenderCode(patientRequestDto: PatientRequestDto) {
        (codeRepository.findByIdCodeGroupAndIdCode(CODE_GROUP_GENDER, patientRequestDto.genderCode)
            ?: throw IllegalArgumentException("${CODE_GROUP_GENDER}를 확인해주세요"))
    }

    private fun validatedPatientInHospital(
        hospital: Hospital,
        patientRequestDto: PatientRequestDto
    ) {
        require(
            !patientRepository.existsByHospitalAndPatientRegistrationNumber(
                hospital, patientRequestDto.patientRegistrationNumber
            )
        ) { throw IllegalArgumentException("${hospital.hospitalName}에 이미 등록된 환자입니다") }
    }
}
