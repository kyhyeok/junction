package hd.junction.patient.presentation

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/patients")
class PatientController(
) {
    @PostMapping()
    fun createPatient(): String {
        return "Patient Created"
    }

    @PatchMapping()
    fun updatePatient(): String {
        return "Patient Updated"
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