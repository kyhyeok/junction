package hd.junction.visit.domain

import hd.junction.hospital.domain.Hospital
import hd.junction.patient.domain.Patient
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDate

@Entity
@Table(name = "visit")
class Visit(
    @Column(length = 10, nullable = false)
    val reservationDate: LocalDate,

    @Column(length = 10, nullable = false)
    val visitStateCode: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    val hospital: Hospital,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    val patient: Patient,

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
) {
}