package hd.junction.patient.infrastructure

import hd.junction.patient.domain.Patient
import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository : JpaRepository<Patient, Long> {
}