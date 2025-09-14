package hd.junction.common.util

import hd.junction.hospital.domain.Hospital
import hd.junction.patient.infrastructure.PatientRepository
import kotlin.random.Random

object RandomUtils {
    /**
     * 13자리 숫자를 각 자리마다 1~9 랜덤하게 환자 등록 번호 생성 (예: 1234567890123)
     * init 데이터 환자 수는 많아도 100내외 예정으로 중복 발생 확률 매우 낮음
     * 실제 시스템에서 UUID 또는 다른 고유값 생성 로직 사용 또는 중복 체크 필요
     * 운영에서 사용할 때 데이터가 너무 많이 쌓일 경우 중복 발생 확률이 높아질 수 있음
     */
    fun generateRandomPatientRegistrationNumber(): String {
        return buildString {
            repeat(13) {
                append(Random.nextInt(0, 10))
            }
        }
    }

    fun getUniquePatientRegistrationNumber(
        hospital: Hospital,
        initialPatientRegistrationNumber: String,
        patientRepository: PatientRepository
    ): String {
        var patientRegistrationNumber = initialPatientRegistrationNumber

        while (patientRepository.existsByHospitalAndPatientRegistrationNumber(hospital, patientRegistrationNumber)) {
            patientRegistrationNumber = generateRandomPatientRegistrationNumber()
        }

        return patientRegistrationNumber
    }
}