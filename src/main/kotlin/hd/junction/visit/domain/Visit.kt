package hd.junction.visit.domain

import hd.junction.hospital.domain.Hospital
import hd.junction.patient.domain.Patient
import hd.junction.visit.dto.request.VisitUpdateRequestDto
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
    companion object {
        fun create(
            reservationDate: LocalDate,
            visitStateCode: String,
            hospital: Hospital,
            patient: Patient
        ): Visit {
            return Visit(
                reservationDate = reservationDate,
                visitStateCode = visitStateCode,
                hospital = hospital,
                patient = patient
            )
        }
    }

    fun update(
        visitUpdateRequestDto: VisitUpdateRequestDto
    ): Visit {
        return Visit(
            reservationDate = visitUpdateRequestDto.reservationDate,
            visitStateCode = visitUpdateRequestDto.visitStateCode,
            hospital = this.hospital,
            patient = this.patient,
            id = this.id
        )
    }
}