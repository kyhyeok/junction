package hd.junction.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuerydslConfig(
    private val em: EntityManager
) {

    @Bean
    fun querydslFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}