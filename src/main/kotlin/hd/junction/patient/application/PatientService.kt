package hd.junction.patient.application

import hd.junction.codes.infrastructure.CodeRepository
import hd.junction.common.util.RandomUtils.generateRandomPatientRegistrationNumber
import hd.junction.common.util.RandomUtils.getUniquePatientRegistrationNumber
import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.request.PatientSearchRequestDto
import hd.junction.patient.dto.response.PatientPageResponseDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.dto.response.PatientVisitResponseDto
import hd.junction.patient.infrastructure.PatientQuerydslRepository
import hd.junction.patient.infrastructure.PatientRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PatientService(
    private val codeRepository: CodeRepository,
    private val hospitalRepository: HospitalRepository,
    private val patientRepository: PatientRepository,
    private val patientQuerydslRepository: PatientQuerydslRepository,
) {
    companion object {
        private const val CODE_GROUP_GENDER: String = "성별코드"
    }
    @Transactional
    fun createPatient(
        patientRequestDto: PatientRequestDto
    ): PatientResponseDto {
        validatedGenderCode(patientRequestDto)

        val hospital = hospitalRepository.findById(patientRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        val patientRegistrationNumber = getUniquePatientRegistrationNumber(
            hospital, generateRandomPatientRegistrationNumber(), patientRepository
        )

        return Patient.create(patientRequestDto, hospital, patientRegistrationNumber)
            .let { patient -> patientRepository.save(patient) }
            .let { patient -> PatientResponseDto.of(patient) }
    }

    @Transactional
    fun updatePatient(
        id: Long, patientRequestDto: PatientRequestDto
    ): PatientResponseDto {
        val foundPatient = patientRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 환자입니다") }

        validatedGenderCode(patientRequestDto)

        val hospital = hospitalRepository.findById(patientRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        return foundPatient.update(patientRequestDto, hospital)
            .let { patient -> patientRepository.save(patient) }
            .let { patient -> PatientResponseDto.of(patient) }
    }

    @Transactional
    fun deletePatient(
        id: Long
    ) {
        val foundPatient = patientRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 환자입니다") }

        patientRepository.delete(foundPatient)
    }

    @Transactional(readOnly = true)
    fun getPatientDetail(
        id: Long
    ): PatientVisitResponseDto {
        val foundPatient = patientRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 환자입니다") }

        return PatientVisitResponseDto.of(foundPatient, foundPatient.visits)
    }

    @Transactional(readOnly = true)
    fun getPatientWithPage(
        patientSearchRequestDto: PatientSearchRequestDto,
        pageable: Pageable
    ): Page<PatientPageResponseDto> {
        return patientQuerydslRepository.getPatientWithPage(patientSearchRequestDto, pageable)
    }


    //  PatientRequestDto에서 검증하지만, 혹시 모를 잘못된 코드값이 들어올 경우를 대비
    private fun validatedGenderCode(
        patientRequestDto: PatientRequestDto
    ) {
        (codeRepository.findByIdCodeGroupAndIdCode(CODE_GROUP_GENDER, patientRequestDto.genderCode)
            ?: throw IllegalArgumentException("${CODE_GROUP_GENDER}를 확인해주세요"))
    }
}
