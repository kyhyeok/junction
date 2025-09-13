package hd.junction.visit.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/visits")
class VisitController(
) {
    @PostMapping()
    fun createVisit(

    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CREATED).body("Visit Created")
    }

    @PatchMapping()
    fun updateVisit(

    ): ResponseEntity<String> {
        return ResponseEntity.ok("Visit Updated")
    }

    @DeleteMapping()
    fun deleteVisit(

    ): ResponseEntity<String> {
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getVisitDetail(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<String> {
        return ResponseEntity.ok("Visit getVisitDetail $id")
    }
}

