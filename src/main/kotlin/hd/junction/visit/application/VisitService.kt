package hd.junction.visit.application

import hd.junction.common.util.RandomUtils.generateRandomPatientRegistrationNumber
import hd.junction.common.util.RandomUtils.getUniquePatientRegistrationNumber
import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.infrastructure.PatientRepository
import hd.junction.visit.domain.Visit
import hd.junction.visit.dto.request.VisitCreateRequestDto
import hd.junction.visit.dto.request.VisitUpdateRequestDto
import hd.junction.visit.dto.response.VisitResponseDto
import hd.junction.visit.infrastructure.VisitRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VisitService(
    private val visitRepository: VisitRepository,
    private val hospitalRepository: HospitalRepository,
    private val patientRepository: PatientRepository
) {
    @Transactional
    fun createVisit(
        VisitCreateRequestDto: VisitCreateRequestDto
    ): VisitResponseDto {
        val foundHospital = hospitalRepository.findById(VisitCreateRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        val patientRegistrationNumber = getUniquePatientRegistrationNumber(
            foundHospital, generateRandomPatientRegistrationNumber(), patientRepository
        )

        val patient = getOrCreatePatient(VisitCreateRequestDto, foundHospital, patientRegistrationNumber)

        return Visit.create(
            VisitCreateRequestDto.reservationDate,
            VisitCreateRequestDto.visitStateCode,
            foundHospital,
            patient
        ).let { visit ->
            visitRepository.save(visit).let { savedVisit ->
                VisitResponseDto.of(savedVisit)
            }
        }
    }

    @Transactional
    fun updateVisit(
        id: Long,
        visitUpdateRequestDto: VisitUpdateRequestDto
    ): VisitResponseDto {
        val foundVisit = visitRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 방문 환자 정보입니다") }

        val updatedVisit = foundVisit.update(visitUpdateRequestDto)
        val savedUpdate = visitRepository.save(updatedVisit)
        return VisitResponseDto.of(savedUpdate)
    }

    @Transactional
    fun deleteVisit(
        id: Long
    ) {
        val foundVisit = visitRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 방문 환자 정보입니다") }

        visitRepository.delete(foundVisit)
    }

    @Transactional(readOnly = false)
    fun getVisitDetail(id: Long): VisitResponseDto {
        val foundVisit = visitRepository.findById(id)
            .orElseThrow { IllegalArgumentException("확인되지 않은 방문 환자 정보입니다") }

        return VisitResponseDto.of(foundVisit)
    }

    private fun getOrCreatePatient(
        VisitCreateRequestDto: VisitCreateRequestDto,
        hospital: Hospital,
        patientRegistrationNumber: String
    ): Patient {
        val patient = when {
            VisitCreateRequestDto.patientId != null -> patientRepository.findById(VisitCreateRequestDto.patientId)
                .orElseThrow { IllegalArgumentException("확인되지 않은 환자 정보입니다") }

            else -> VisitCreateRequestDto.patientRequest?.let { request ->
                patientRepository.save(
                    Patient.create(request, hospital, patientRegistrationNumber)
                )
            } ?: throw IllegalArgumentException("환자 정보가 제공되지 않았습니다")
        }
        return patient
    }
}