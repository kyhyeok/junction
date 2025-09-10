package hd.junction.visit.infrastructure

import hd.junction.visit.domain.Visit
import org.springframework.data.jpa.repository.JpaRepository

interface VisitRepository : JpaRepository<Visit, Long> {
}