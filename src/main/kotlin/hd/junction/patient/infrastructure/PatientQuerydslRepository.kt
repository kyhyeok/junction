package hd.junction.patient.infrastructure

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class PatientQuerydslRepository(
    private val queryFactory: JPAQueryFactory
) {
}