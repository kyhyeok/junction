package hd.junction.visit.presentation

import hd.junction.visit.application.VisitService
import hd.junction.visit.dto.request.VisitRequestDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/visits")
class VisitController(
    private val visitService: VisitService,
) {
    @PostMapping()
    fun createVisit(
        @RequestBody visitRequestDto: VisitRequestDto
    ): ResponseEntity<String> {
        visitService.createVisit(visitRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body("")
    }

    @PatchMapping()
    fun updateVisit(

    ): ResponseEntity<String> {
        visitService.updateVisit()
        return ResponseEntity.ok("")
    }

    @DeleteMapping()
    fun deleteVisit(

    ): ResponseEntity<String> {
        visitService.deleteVisit()
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getVisitDetail(
        @PathVariable("id", required = true) id: Long
    ): ResponseEntity<String> {
        visitService.getVisitDetail()
        return ResponseEntity.ok("")
    }
}

