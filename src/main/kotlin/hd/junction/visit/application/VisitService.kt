package hd.junction.visit.application

import hd.junction.common.util.RandomUtils.generateRandomPatientRegistrationNumber
import hd.junction.common.util.RandomUtils.getUniquePatientRegistrationNumber
import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.infrastructure.PatientRepository
import hd.junction.visit.domain.Visit
import hd.junction.visit.dto.request.VisitRequestDto
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
        visitRequestDto: VisitRequestDto
    ) {
        val hospital = hospitalRepository.findById(visitRequestDto.hospitalId)
            .orElseThrow { IllegalArgumentException("확인되지 않은 병원 정보입니다") }

        val patientRegistrationNumber = getUniquePatientRegistrationNumber(
            hospital, generateRandomPatientRegistrationNumber(), patientRepository
        )

        val patient = getOrCreatePatient(visitRequestDto, hospital, patientRegistrationNumber)

        val visit = Visit.create(visitRequestDto.reservationDate, visitRequestDto.visitStateCode, hospital, patient)
        visitRepository.save(visit)
    }

    @Transactional
    fun updateVisit() {

    }

    @Transactional
    fun deleteVisit() {

    }

    @Transactional(readOnly = false)
    fun getVisitDetail() {

    }

    private fun getOrCreatePatient(
        visitRequestDto: VisitRequestDto,
        hospital: Hospital,
        patientRegistrationNumber: String
    ): Patient {
        val patient = when {
            visitRequestDto.patientId != null -> patientRepository.findById(visitRequestDto.patientId)
                .orElseThrow { IllegalArgumentException("확인되지 않은 환자 정보입니다") }

            else -> visitRequestDto.patientRequest?.let { request ->
                patientRepository.save(
                    Patient.create(request, hospital, patientRegistrationNumber)
                )
            } ?: throw IllegalArgumentException("환자 정보가 제공되지 않았습니다")
        }
        return patient
    }
}