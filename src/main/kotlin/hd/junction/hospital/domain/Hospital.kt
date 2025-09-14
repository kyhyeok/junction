package hd.junction.hospital.domain

import hd.junction.patient.domain.Patient
import hd.junction.visit.domain.Visit
import jakarta.persistence.*
import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "hospital")
class Hospital(
    @Column(length = 24, nullable = false)
    val hospitalName: String,

    @Column(length = 20, nullable = false)
    val nursingInstitutionNumber: String,

    @Column(length = 10, nullable = false)
    val hospitalDirector: String,

    @OneToMany(mappedBy = "hospital", cascade = [ALL], orphanRemoval = true)
    val patients: MutableList<Patient> = mutableListOf(),

    @OneToMany(mappedBy = "hospital", cascade = [ALL], orphanRemoval = true)
    val visits: MutableList<Visit> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long? = null,
)