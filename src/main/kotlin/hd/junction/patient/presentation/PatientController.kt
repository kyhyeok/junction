package hd.junction.patient.presentation

import hd.junction.patient.dto.request.PatientRequestDto
import hd.junction.patient.dto.response.PatientResponseDto
import hd.junction.patient.service.PatientService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(
    private val patientService: PatientService
) {
    @PostMapping()
    fun createPatient(@RequestBody patientRequestDto: PatientRequestDto): PatientResponseDto {
        return patientService.createPatient(patientRequestDto)
    }

    @PatchMapping("/{id}")
    fun updatePatient(
        @PathVariable("id", required = true) id: Long,
        @RequestBody patientRequestDto: PatientRequestDto
    ): PatientResponseDto {
        return patientService.updatePatient(id, patientRequestDto)
    }

    @DeleteMapping()
    fun deletePatient(): String {
        return "Patient Deleted"
    }

    @GetMapping("/{id}")
    fun getPatientDetail(
        @PathVariable("id", required = true) id: Long
    ): String {
        return "Patient getPatientDetail $id"
    }

    @GetMapping()
    fun getPatientWithPage(): String {
        return "Patient getPatientWithPage"
    }
}