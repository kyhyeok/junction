package hd.junction.codes.domain

import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY
import java.io.Serializable

@Embeddable
data class CodeId(
    @Column(name = "code_group", length = 10, nullable = false)
    val codeGroup: String,

    @Column(name = "code", length = 10, nullable = false)
    val code: String
) : Serializable

@Entity
@Table(name = "code")
class Code(
    @EmbeddedId
    val id: CodeId,

    @Column(name = "code_name", length = 10, nullable = false)
    val codeName: String,

    @ManyToOne(fetch = LAZY)
    @MapsId("codeGroup")
    @JoinColumn(name = "code_group", columnDefinition = "varchar(10)")
    val codeGroup: CodeGroup,
)

@Entity
@Table(name = "code_group")
class CodeGroup(
    @Column(length = 100, nullable = false)
    val description: String,

    @Column(length = 10, nullable = false)
    val codeGroupName: String,

    @OneToMany(mappedBy = "codeGroup")
    val codes: MutableList<Code> = mutableListOf(),

    @Id
    @Column(name = "code_group", length = 10, nullable = false)
    val codeGroup: String,
)