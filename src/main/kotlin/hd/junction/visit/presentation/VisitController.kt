package hd.junction.visit.presentation

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/visits")
class VisitController(
) {
    @PostMapping()
    fun createVisit(): String {
        return "Visit Created"
    }

    @PatchMapping()
    fun updateVisit(): String {
        return "Visit Updated"
    }

    @DeleteMapping()
    fun deleteVisit(): String {
        return "Visit Deleted"
    }

    @GetMapping("/{id}")
    fun getVisitDetail(
        @PathVariable("id", required = true) id: Long
    ): String {
        return "Visit getVisitDetail $id"
    }

    @GetMapping()
    fun getVisitWithPage(): String {
        return "Visit getPatientWithPage"
    }
}