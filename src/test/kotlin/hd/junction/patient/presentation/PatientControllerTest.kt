package hd.junction.patient.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import hd.junction.hospital.infrastructure.HospitalRepository
import hd.junction.patient.application.PatientService
import hd.junction.patient.fixture.PatientFixture.testPatientRequestFixture
import hd.junction.patient.infrastructure.PatientRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,

    private val objectMapper: ObjectMapper,

    private val patientService: PatientService,

    private val hospitalRepository: HospitalRepository,

    private val patientRepository: PatientRepository,
) {
    companion object {
        private const val API_END_POINT = "/api/v1/patients"
    }

    @Test
    @DisplayName("환자 등록 성공")
    fun createPatient_When_ValidRequest() {
        // given
        val request = testPatientRequestFixture(
            patientName = "등록 김환자",
            phoneNumber = "010-1122-6655",
            genderCode = "F"
        )
        val requestJson = objectMapper.writeValueAsString(request)
        val hospital = hospitalRepository.findById(request.hospitalId).get()

        // when & then
        mockMvc
            .perform(
                post(API_END_POINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.patientName").value("등록 김환자"))
            .andExpect(jsonPath("$.genderCode").value("F"))
            .andExpect(jsonPath("$.birthDay").value(request.birthDay.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(request.phoneNumber))
            .andExpect(jsonPath("$.hospital.id").value(hospital.id))
    }

    @Test
    @DisplayName("환자 수정 성공")
    fun updatePatient_When_ValidRequest() {
        // given
        val createRequest = testPatientRequestFixture(
            patientName = "수정 전 김환자",
            phoneNumber = "010-1122-6655",
            birthDay = null
        )
        val patient = patientService.createPatient(createRequest)

        // 수정 전 데이터 검증
        with(patient) {
            assertThat(id).isEqualTo(patient.id)
            assertThat(patientName).isEqualTo("수정 전 김환자")
            assertThat(patientRegistrationNumber).isEqualTo(patient.patientRegistrationNumber)
            assertThat(genderCode).isEqualTo(patient.genderCode)
            assertThat(birthDay).isNull()
            assertThat(phoneNumber).isEqualTo("010-1122-6655")
        }

        val updateBirthDay = LocalDate.of(2000, 1, 1)
        val updateRequest = testPatientRequestFixture(
            patientName = "수정 후 박환자",
            phoneNumber = "010-3333-3334",
            birthDay = updateBirthDay,
        )
        val updateJson = objectMapper.writeValueAsString(updateRequest)

        // when & then
        mockMvc
            .perform(
                patch("${API_END_POINT}/${patient.id}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(patient.id))
            .andExpect(jsonPath("$.patientName").value("수정 후 박환자"))
            .andExpect(jsonPath("$.genderCode").value(updateRequest.genderCode))
            .andExpect(jsonPath("$.birthDay").value(updateBirthDay.toString()))
            .andExpect(jsonPath("$.phoneNumber").value("010-3333-3334"))
            .andExpect(jsonPath("$.hospital.id").value(patient.hospital.id))

        patientService.deletePatient(patient.id)
    }

    @Test
    @DisplayName("환자 삭제 성공")
    fun deletePatient_When_ValidPatientExists() {
        // given
        val request = testPatientRequestFixture()
        val patient = patientService.createPatient(request)

        // 삭제 전 데이터 검증
        assertThat(patient).isNotNull()

        // when
        mockMvc
            .perform(delete("$API_END_POINT/${patient.id}"))
            .andExpect(status().isNoContent())

        // then
        val deleted = patientRepository.findById(patient.id)
        assertThat(deleted).isEmpty
    }

    @Test
    @Transactional
    @DisplayName("환자 상세 조회 - 방문 기록 있음")
    fun getPatientDetail_When_Visited() {
        // given
        val patientId = 1L

        // when
        val mvcResult = mockMvc
            .perform(get("${API_END_POINT}/${patientId}"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val responseJson = mvcResult.response.contentAsString
        val node = objectMapper.readTree(responseJson)
        val patient = patientRepository.findById(patientId).get()

        with(node) {
            assertThat(get("id").asLong()).isEqualTo(patient.id)
            assertThat(get("patientName").asText()).isEqualTo(patient.patientName)
            assertThat(get("patientRegistrationNumber").asText()).isEqualTo(patient.patientRegistrationNumber)
            assertThat(get("genderCode").asText()).isEqualTo(patient.genderCode)
            assertThat(get("birthDay").asText()).isEqualTo(patient.birthDay.toString())
            assertThat(get("phoneNumber").asText()).isEqualTo(patient.phoneNumber)
        }

        val visits = patient.visits
        val visitsNode = node["visits"]
        assertThat(visitsNode.isArray).isTrue()
        assertThat(visitsNode.size()).isEqualTo(visits.size)

        // 방문정보 & 병원정보 검증
        visitsNode.forEachIndexed { idx, visitNode ->
            // 방문 정보 검증
            with(visitNode) {
                assertThat(get("id").asLong()).isEqualTo(visits[idx].id)
                assertThat(get("reservationDate").asText()).isEqualTo(visits[idx].reservationDate.toString())
                assertThat(get("visitStateCode").asText()).isEqualTo(visits[idx].visitStateCode)
            }

            // 병원 정보 검증
            val hospitalNode = visitNode["hospital"]
            with(hospitalNode) {
                assertThat(get("id").asLong()).isEqualTo(visits[idx].hospital.id)
                assertThat(get("hospitalName").asText()).isEqualTo(visits[idx].hospital.hospitalName)
                assertThat(get("nursingInstitutionNumber").asText()).isEqualTo(visits[idx].hospital.nursingInstitutionNumber)
                assertThat(get("hospitalDirector").asText()).isEqualTo(visits[idx].hospital.hospitalDirector)
            }
        }
    }

    @Test
    @DisplayName("환자 상세 조회 - 방문 기록 없음")
    fun getPatientDetail_When_NotVisited() {
        // given
        val request = testPatientRequestFixture(
            patientName = "환자 조회 나환자",
            phoneNumber = "010-1010-2020",
            birthDay = null,
            genderCode = "M"
        )
        val patient = patientService.createPatient(request)

        // when
        val mvcResult = mockMvc.perform(get("${API_END_POINT}/${patient.id}"))
            .andExpect(status().isOk)
            .andReturn()

        // then
        val responseJson = mvcResult.response.contentAsString
        val node = objectMapper.readTree(responseJson)

        with(node) {
            assertThat(get("id").asLong()).isEqualTo(patient.id)
            assertThat(get("patientName").asText()).isEqualTo("환자 조회 나환자")
            assertThat(get("genderCode").asText()).isEqualTo("M")
            assertThat(get("phoneNumber").asText()).isEqualTo("010-1010-2020")
            assertThat(get("birthDay").isNull).isTrue
        }

        val visitsNode = node["visits"]
        assertThat(visitsNode.isArray).isTrue()
        assertThat(visitsNode.size()).isEqualTo(0)
    }

    @Test
    @Transactional
    @DisplayName("환자 목록 조회 - 환자 이름 검색")
    fun getPatientWithPage_When_SearchPatientName() {
        // given
        val patientName = "검색용환자1"
        listOf(patientName, "검색용환자2", "검색용환자3")
            .map { testPatientRequestFixture(patientName = it) }
            .forEach { patientService.createPatient(it) }

        // when & then
        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("patientName", patientName)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].patientName").value(patientName))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(10))
    }

    @Test
    @Transactional
    @DisplayName("환자 목록 조회 - 환자 등록 번호 검색")
    fun getPatientWithPage_When_SearchPatientRegistrationNumber() {
        // given 자동생성이라 DataInitializer 에서 미리 생성된 환자 중복되지 않는 번호로 검색
        val patientRegistrationNumber = "1122331122331"

        // when & then
        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("patientRegistrationNumber", patientRegistrationNumber)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].patientRegistrationNumber").value(patientRegistrationNumber))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(10))
    }

    @Test
    @Transactional
    @DisplayName("환자 목록 조회 - 생년월일 검색")
    fun getPatientWithPage_When_SearchBrithDay() {
        // given
        val birthDay = LocalDate.of(2000, 1, 31)
        val birthDays = listOf(birthDay, birthDay,LocalDate.of(2000, 1, 1))

        birthDays.forEach { birthDay ->
            patientService.createPatient(testPatientRequestFixture(birthDay = birthDay))
        }

        // when & then
        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("birthDay", birthDay.toString())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].birthDay").value(birthDay.toString()))
            .andExpect(jsonPath("$.content[1].birthDay").value(birthDay.toString()))
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(10))
    }

    @Test
    @DisplayName("환자 목록 조회 - 첫 페이지 조회")
    fun getPatientWithPage_When_FirstPage() {
        // given
        val pageSize = 10
        val totalCount = patientRepository.count()

        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("page", "1")
                    .param("size", pageSize.toString())
                    .param("sort", "id,asc")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(pageSize))
            .andExpect(jsonPath("$.number").value(0))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.totalElements").value(totalCount))
            .andExpect(jsonPath("$.first").value(true))
            .andExpect(jsonPath("$.last").value(false))
    }

    @Test
    @DisplayName("환자 목록 조회 - 중간 페이지 조회")
    fun getPatientWithPage_When_MiddlePage() {
        val pageSize = 10
        val pageNumber = 4
        val totalCount = patientRepository.count()

        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("page", pageNumber.toString())
                    .param("size", pageSize.toString())
                    .param("sort", "id,asc")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content.length()").value(pageSize))
            .andExpect(jsonPath("$.number").value(pageNumber - 1))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.totalElements").value(totalCount))
            .andExpect(jsonPath("$.first").value(false))
            .andExpect(jsonPath("$.last").value(false))
    }

    @Test
    @DisplayName("환자 목록 조회 - 마지막 페이지 조회")
    fun getPatientWithPage_When_LastPage() {
        val pageSize = 10
        val totalCount = patientRepository.count()
        val pageInfo = calculatePageInfo(totalCount, pageSize)

        mockMvc
            .perform(
                get(API_END_POINT)
                    .param("page", pageInfo.lastPageRequest.toString())
                    .param("size", pageSize.toString())
                    .param("sort", "id,asc")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.number").value(pageInfo.expectedNumber))
            .andExpect(jsonPath("$.size").value(pageSize))
            .andExpect(jsonPath("$.totalElements").value(totalCount))
            .andExpect(jsonPath("$.totalPages").value(pageInfo.totalPages))
            .andExpect(jsonPath("$.numberOfElements").value(pageInfo.numberOfElements))
            .andExpect(jsonPath("$.first").value(false))
            .andExpect(jsonPath("$.last").value(true))
    }

    private fun calculatePageInfo(totalCount: Long, pageSize: Int) = object {
        val totalPages = ((totalCount + pageSize - 1) / pageSize).toInt()
        val lastPageRequest = totalPages  // 1-based request
        val expectedNumber = totalPages - 1  // 0-based response
        val numberOfElements = if (totalCount % pageSize == 0L) pageSize else (totalCount % pageSize).toInt()
    }
}