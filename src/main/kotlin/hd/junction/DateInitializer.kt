package hd.junction

import hd.junction.common.util.RandomUtils.generateRandomPatientRegistrationNumber
import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.domain.Patient
import hd.junction.patient.infrastructure.PatientRepository
import hd.junction.visit.domain.Visit
import hd.junction.visit.infrastructure.VisitRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.random.Random

@Component
class DateInitializer(
    private val hospitalRepository: HospitalRepository,
    private val patientRepository: PatientRepository,
    private val visitRepository: VisitRepository,
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments?) {
        // 병원 10개 생성
        for (i in 1..10) {
            val hospital = Hospital(
                hospitalName = "${i}번째 Hospital",
                nursingInstitutionNumber = "${i}-hospital",
                hospitalDirector = "${i}병원의 병원장"
            )

            hospitalRepository.save(hospital)
        }

        // 테스트 검증을 위한 특정 환자 1명 생성
        // patientRegistrationNumber, birthDay
        val patient = Patient(
            patientName = "나환자",
            patientRegistrationNumber = "1122331122331",
            genderCode = "M",
            birthDay = LocalDate.of(1990, 1, 1),
            phoneNumber = "010-1234-4321",
            hospital = hospitalRepository.findById(3L).get()
        )
        patientRepository.save(patient)

        // 환자 30명 생성
        for (i in 1..30) {
            val year = Random.nextInt(1950, 2006)
            val month = Random.nextInt(1, 13)
            val day = Random.nextInt(1, 28)

            val shouldBirthDayBeNull = Random.nextInt(100) < 40
            val birthDay = LocalDate.of(year, month, day)
            val finalBirthDay = if (shouldBirthDayBeNull) null else birthDay

            val middleNumber = Random.nextInt(1000, 10000)
            val lastNumber = Random.nextInt(1000, 10000)

            val shouldPhoneNumberBeNull = Random.nextInt(100) < 40
            val phoneNumber = "010-${middleNumber}-${lastNumber}"
            val finalPhoneNumber = if (shouldPhoneNumberBeNull) null else phoneNumber

            val genderCode = if (Random.nextBoolean()) "M" else "F"

            val hospitalCount = Random.nextInt(1, 6)
            val hospitalIds = (1..5).shuffled().take(hospitalCount)


            // 각 환자마다 1~5개의 병원 랜덤 배정
            hospitalIds.forEach { hospitalId ->
                val patientRegistrationNumber = generateRandomPatientRegistrationNumber()

                val patient = Patient(
                    patientName = "김환자${i}",
                    patientRegistrationNumber = patientRegistrationNumber,
                    genderCode = genderCode,
                    birthDay = finalBirthDay,
                    phoneNumber = finalPhoneNumber,
                    hospital = hospitalRepository.findById(hospitalId.toLong()).get()
                )
                patientRepository.save(patient)
            }
        }

        val allPatients = patientRepository.findAll()
        val visits = mutableListOf<Visit>()
        val today = LocalDate.now()

        // 첫 번째 환자는 테스트 검증을 위해 직접 데이터 저장
        val firstVisit1 = createVisit(today.plusDays(-2), "2")
        visitRepository.save(firstVisit1)
        val firstVisit2 = createVisit(today.plusDays(0), "1")
        visitRepository.save(firstVisit2)

        // 첫 번째 환재 제외, 각 환자마다 0~10개의 방문 기록 생성
        allPatients.drop(1).forEach { patient ->
            if (Random.nextInt(100) < 80) {  // 80% 환자만 방문 기록 (방문 X, 환자 등록만 고려)
                val visitCount = Random.nextInt(1, 10)
                repeat(visitCount) {
                    val dayOffset = Random.nextInt(-5, 3)
                    val reservationDate = today.plusDays(dayOffset.toLong())

                    val visitStateCode = when {
                        reservationDate.isEqual(today) -> "1"  // 오늘 날짜면 1 (방문중)
                        reservationDate.isAfter(today) -> "3"  // 미래 시점은 3 (취소)
                        else -> if (Random.nextBoolean()) "2" else "3"  // 과거 시점은 2 (종료) 또는 3 (취소)
                    }

                    val randomHospitalId = Random.nextInt(1, 6).toLong()
                    val randomHospital = hospitalRepository.findById(randomHospitalId).get()

                    val visit = Visit(
                        reservationDate = reservationDate,
                        visitStateCode = visitStateCode,
                        hospital = randomHospital,
                        patient = patient
                    )
                    visits.add(visit)

                    visitRepository.saveAll(visits)
                    visits.clear()
                }
            }
        }
    }

    private fun createVisit(
        reservationDate: LocalDate,
        visitStateCode: String,
        hospital: Hospital = hospitalRepository.findById(1L).get(),
        patient: Patient = patientRepository.findById(1L).get(),
    ): Visit {
        return Visit(
            reservationDate = reservationDate,
            visitStateCode = visitStateCode,
            hospital = hospital,
            patient = patient
        )
    }
}