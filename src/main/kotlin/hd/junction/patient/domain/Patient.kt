package hd.junction.patient.domain

import hd.junction.hospital.domain.Hospital
import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.visit.domain.Visit
import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDate

@Entity
@Table(name = "patient")
class Patient(
    @Column(length = 45, nullable = false)
    val patientName: String,

    @Column(length = 13, nullable = false, unique = true)
    val patientRegistrationNumber: String,

    @Column(length = 10, nullable = false)
    val genderCode: String,

    @Column(length = 10, nullable = true)
    val birthDay: LocalDate?,

    @Column(length = 20, nullable = true)
    val phoneNumber: String?,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    val hospital: Hospital,

    @OneToMany(mappedBy = "patient", cascade = [ALL], orphanRemoval = true)
    var visits: MutableList<Visit> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
) {
    companion object {
        fun create(
            patientCreateRequestDto: PatientRequestDto,
            hospital: Hospital,
        ): Patient {
            return Patient(
                patientCreateRequestDto.patientName,
                patientCreateRequestDto.patientRegistrationNumber,
                patientCreateRequestDto.genderCode,
                patientCreateRequestDto.birthDay,
                patientCreateRequestDto.phoneNumber,
                hospital
            )
        }
    }
}