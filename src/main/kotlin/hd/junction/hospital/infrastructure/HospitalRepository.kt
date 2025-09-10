package hd.junction.hospital.infrastructure

import hd.junction.hospital.domain.Hospital
import org.springframework.data.jpa.repository.JpaRepository

interface HospitalRepository : JpaRepository<Hospital, Long> {
}