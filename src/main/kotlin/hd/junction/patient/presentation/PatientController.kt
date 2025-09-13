package hd.junction.patient.presentation

import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.request.PatientSearchRequestDto
import hd.junction.patient.dto.response.PatientPageResponseDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.dto.response.PatientVisitResponseDto
import hd.junction.patient.service.PatientService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(
    private val patientService: PatientService
) {
    @PostMapping
    fun createPatient(
        @RequestBody patientRequestDto: PatientRequestDto
    ): PatientResponseDto {
        return patientService.createPatient(patientRequestDto)
    }

    @PatchMapping("/{id}")
    fun updatePatient(
        @PathVariable("id", required = true) id: Long,
        @RequestBody patientRequestDto: PatientRequestDto
    ): PatientResponseDto {
        return patientService.updatePatient(id, patientRequestDto)
    }

    @DeleteMapping("/{id}")
    fun deletePatient(
        @PathVariable("id", required = true) id: Long
    ) {
        patientService.deletePatient(id)
    }

    @GetMapping("/{id}")
    fun getPatientDetail(
        @PathVariable("id", required = true) id: Long
    ): PatientVisitResponseDto {
        return patientService.getPatientDetail(id)
    }

    @GetMapping
    fun getPatientWithPage(
        @ModelAttribute patientSearchRequestDto: PatientSearchRequestDto
    ): List<PatientPageResponseDto> {
        return patientService.getPatientWithPage(patientSearchRequestDto)
    }
}