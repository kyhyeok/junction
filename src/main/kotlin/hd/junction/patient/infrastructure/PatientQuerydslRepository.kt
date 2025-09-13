package hd.junction.patient.infrastructure

import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import hd.junction.patient.domain.QPatient.patient
import hd.junction.patient.dto.request.PatientSearchRequestDto
import hd.junction.patient.dto.response.PatientPageResponseDto
import hd.junction.visit.domain.QVisit.visit
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component

@Component
class PatientQuerydslRepository(
    private val queryFactory: JPAQueryFactory
) {

    // 최근 방문 (방문 상태 코드 2) 기준으로 환자 목록 조회 - 방문 상태가 종료일 떄 최근 방문이라 판단
    fun getPatientWithPage(
        patientSearchRequestDto: PatientSearchRequestDto,
        pageable: Pageable
    ): Page<PatientPageResponseDto> {
        val visitStateCodeEnd = "2"

        val countQuery = queryFactory
            .select(patient.count())
            .from(patient)
            .where(
                patientSearchRequestDto.patientName?.let { patient.patientName.eq(it) },
                patientSearchRequestDto.patientRegistrationNumber?.let { patient.patientRegistrationNumber.eq(it) },
                patientSearchRequestDto.birthDay?.let { patient.birthDay.eq(it) }
            )

        val query = queryFactory
            .select(
                Projections.constructor(
                    PatientPageResponseDto::class.java,
                    patient.id,
                    patient.patientName,
                    patient.patientRegistrationNumber,
                    patient.genderCode,
                    patient.birthDay,
                    patient.phoneNumber,
                    JPAExpressions
                        .select(visit.reservationDate.max())
                        .from(visit)
                        .where(
                            visit.patient.id.eq(patient.id),
                            visit.visitStateCode.eq(visitStateCodeEnd)
                        )
                        .limit(1)
                )
            )
            .from(patient)
            .where(
                patientSearchRequestDto.patientName?.let { patient.patientName.eq(it) },
                patientSearchRequestDto.patientRegistrationNumber?.let { patient.patientRegistrationNumber.eq(it) },
                patientSearchRequestDto.birthDay?.let { patient.birthDay.eq(it) }
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(patient.id.desc())

        return PageableExecutionUtils.getPage(query.fetch(), pageable) { countQuery.fetchOne() ?: 0L }
    }
}