package hd.junction.codes.infrastructure

import hd.junction.codes.domain.Code
import org.springframework.data.jpa.repository.JpaRepository

interface CodeRepository : JpaRepository<Code, Long> {
    fun existsByIdCodeGroupAndIdCode(codeGroup: String, code: String): Boolean
}