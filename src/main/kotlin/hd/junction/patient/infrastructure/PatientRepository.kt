package hd.junction.patient.infrastructure

import hd.junction.hospital.domain.Hospital
import hd.junction.patient.domain.Patient
import org.springframework.data.jpa.repository.JpaRepository

interface PatientRepository : JpaRepository<Patient, Long> {
    fun existsByHospitalAndPatientRegistrationNumber(hospital: Hospital, patientRegistrationNumber: String): Boolean
}