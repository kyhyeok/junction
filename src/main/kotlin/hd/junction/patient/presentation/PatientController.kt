package hd.junction.patient.presentation

import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.request.PatientSearchRequestDto
import hd.junction.patient.dto.response.PatientPageResponseDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.dto.response.PatientVisitResponseDto
import hd.junction.patient.service.PatientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(
    private val patientService: PatientService
) {
    @PostMapping
    fun createPatient(
        @RequestBody patientRequestDto: PatientRequestDto
    ): ResponseEntity<PatientResponseDto> {
        val response = patientService.createPatient(patientRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PatchMapping("/{id}")
    fun updatePatient(
        @PathVariable("id", required = true) id: Long,
        @RequestBody patientRequestDto: PatientRequestDto
    ): ResponseEntity<PatientResponseDto> {
        val response = patientService.updatePatient(id, patientRequestDto)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deletePatient(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<Unit> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getPatientDetail(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<PatientVisitResponseDto> {
        val response = patientService.getPatientDetail(id)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getPatientWithPage(
        @ModelAttribute patientSearchRequestDto: PatientSearchRequestDto,
        @PageableDefault(page = 0, size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<PatientPageResponseDto>> {
        val response = patientService.getPatientWithPage(patientSearchRequestDto, pageable)
        return ResponseEntity.ok(response)
    }
}