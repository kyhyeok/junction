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
        val hospital = Hospital(
            hospitalName = "One Hospital",
            nursingInstitutionNumber = "abcd-qwer",
            hospitalDirector = "원병원 병원장"
        )

        hospitalRepository.save(hospital)
    }
}