package hd.junction.patient.service

import hd.junction.codes.infrastructure.CodeRepository
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.dto.request.PatientCreateRequestDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.infrastructure.PatientRepository
import org.springframework.stereotype.Service

@Service
class PatientService(
    private val codeRepository: CodeRepository,
    private val hospitalRepository: HospitalRepository,
    private val patientRepository: PatientRepository
) {

    fun createPatient(patientCreateRequestDto: PatientCreateRequestDto): PatientResponseDto {
        codeRepository.findByIdCodeGroupAndIdCode("성별코드", patientCreateRequestDto.genderCode)
            ?: throw IllegalArgumentException("성별 입력 코드를 확인해주세요")
        
        val hospital = hospitalRepository.findById(patientCreateRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        return Patient.create(patientCreateRequestDto, hospital)
            .let { patient -> patientRepository.save(patient) }
            .let { patient -> PatientResponseDto.of(patient) }
    }
}
