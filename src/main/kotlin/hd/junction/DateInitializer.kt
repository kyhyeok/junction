package hd.junction

import hd.junction.hospital.domain.Hospital
import hd.junction.hospital.infrastructure.HospitalRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DateInitializer(
    private val hospitalRepository: HospitalRepository
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments?) {
        val hospital1 = Hospital(
            hospitalName = "One Hospital",
            nursingInstitutionNumber = "first-hospital",
            hospitalDirector = "원병원 병원장"
        )

        hospitalRepository.save(hospital1)

        val hospital2 = Hospital(
            hospitalName = "이건 두번쨰 Hospital",
            nursingInstitutionNumber = "second-hospital",
            hospitalDirector = "두번째 병원장"
        )

        hospitalRepository.save(hospital2)
    }
}